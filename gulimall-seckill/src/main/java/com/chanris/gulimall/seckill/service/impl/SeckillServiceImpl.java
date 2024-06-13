package com.chanris.gulimall.seckill.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.chanris.gulimall.common.to.mq.SeckillOrderTo;
import com.chanris.gulimall.common.to.seckill.SeckillSessionTo;
import com.chanris.gulimall.common.to.seckill.SeckillSkuRedisTo;
import com.chanris.gulimall.common.to.seckill.SkuInfoTo;
import com.chanris.gulimall.common.utils.Result;
import com.chanris.gulimall.common.vo.MemberResponseVo;
import com.chanris.gulimall.seckill.feign.CouponFeignService;
import com.chanris.gulimall.seckill.feign.ProductFeignService;
import com.chanris.gulimall.seckill.intercptor.LoginInterceptor;
import com.chanris.gulimall.seckill.service.SeckillService;
import jodd.time.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/12
 * @description
 */
@Slf4j
@Service
public class SeckillServiceImpl implements SeckillService {
    @Resource
    private CouponFeignService couponFeignService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    RedissonClient redissonClient;
    @Resource
    RabbitTemplate rabbitTemplate;

    @Resource
    private ProductFeignService productFeignService;
    private final String SESSIONS_CACHE_PREFIX = "seckill:sessions:";
    private final String SKUKILL_CACHE_PREFIX = "seckill:skus:";
    private final String SKU_STOCK_SEMAPHORE = "seckill:stock:";

    /**
     * 上架最近三天的秒杀商品
     */
    @Override
    public void uploadSeckillSkuLatest3Days() {
        Result<List<SeckillSessionTo>> r = couponFeignService.getLatest3DaysSession();
        if (r.getCode() == 0) {
            List<SeckillSessionTo> data = r.getData();
            // 缓存到redis
            //1.缓存活动信息
            saveSessionInfos(data);
            //2.缓存或懂的相关商品信息
            saveSessionSkuInfos(data);
        }
    }

    /**
     * 获取当前时间可以参加的秒杀活动商品信息
     * @return
     */
    @Override
    public List<SeckillSkuRedisTo> getCurrentSeckillSkus() {
        // 1. 确定当前时间属于哪个秒杀场次
        long time = System.currentTimeMillis();
        Set<String> keys = stringRedisTemplate.keys(SESSIONS_CACHE_PREFIX + "*");
        assert keys != null;
        for (String key : keys) {
            String replace = key.replace(SESSIONS_CACHE_PREFIX, "");
            String[] s = replace.split("_");
            long start = Long.parseLong(s[0]);
            long end = Long.parseLong(s[1]);
            if (start <= time && time <= end) {
                //获得秒杀场次的商品信息
                List<String> range = stringRedisTemplate.opsForList().range(key, -100, 100);
                BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
                assert range != null;
                List<String> list = hashOps.multiGet(range);
                if (list != null) {
                    List<SeckillSkuRedisTo> collect = list.stream().map(item -> JSON.parseObject(item, SeckillSkuRedisTo.class)).toList();
                    return collect;
                }
                break;
            }
        }
        // 2.
        return null;
    }

    /**
     * 获取秒杀商品详情信息
     * @param skuId
     */
    @Override
    public SeckillSkuRedisTo getSkuSeckillInfo(Long skuId) {
        // 找到所有需要秒杀的商品信息
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        Set<String> keys = hashOps.keys();
        if (keys != null && !keys.isEmpty()) {
            String reg = "\\d-"+skuId;
            for (String key : keys) {
                if (Pattern.matches(reg, key)) {
                    String json = hashOps.get(key);
                    SeckillSkuRedisTo seckillSkuRedisTo = JSON.parseObject(json, SeckillSkuRedisTo.class);
                    Long startTime = seckillSkuRedisTo.getStartTime();
                    Long endTime = seckillSkuRedisTo.getEndTime();
                    long current =  System.currentTimeMillis();
                    if (startTime <= current && current <= endTime) {}else{
                        seckillSkuRedisTo.setRandomCode(null);
                    }
                    return seckillSkuRedisTo;
                }
            }
        }
        return null;
    }

    /**
     * 执行秒杀
     * @param killId
     * @param key 随机码
     * @param num 秒杀数量
     * @return
     */
    @Override
    public String kill(String killId, String key, Integer num) {
        MemberResponseVo memberResponseVo = LoginInterceptor.loginUser.get();

        // 获取当前秒杀商品的详细信息
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);

        String json = hashOps.get(killId);
        if (!StrUtil.isNotBlank(json)) {
            return null;
        }
        SeckillSkuRedisTo redisTo = JSON.parseObject(json, SeckillSkuRedisTo.class);
        assert redisTo != null;
        Long startTime = redisTo.getStartTime();
        Long endTime = redisTo.getEndTime();
        long time = System.currentTimeMillis();
        if (time < startTime || time > endTime) {
            return null;
        }
        // 校验随机码
        String randomCode = redisTo.getRandomCode();
        String skuId = redisTo.getPromotionSessionId() + "-" + redisTo.getSkuId();
        if (!randomCode.equals(key) || !killId.equals(skuId)) {
            return null;
        }
        // 验证限购
        if (num > redisTo.getSeckillLimit().intValue()) {
            return null;
        }
        String redisKey = memberResponseVo.getId() + "_" + skuId;
        long ttl = endTime - time;
        Boolean b = stringRedisTemplate.opsForValue().setIfAbsent(redisKey, num.toString(), ttl, TimeUnit.MILLISECONDS);
        // 占位失败
        if (Boolean.FALSE.equals(b)) {
            return null;
        }
        RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + randomCode);
        boolean acquireSuccess = semaphore.tryAcquire(num);
        if (!acquireSuccess) {
            return null;
        }
        //秒杀成功
        String orderSn = IdWorker.getTimeId();
        SeckillOrderTo orderTo = new SeckillOrderTo();
        orderTo.setOrderSn(orderSn);
        orderTo.setMemberId(memberResponseVo.getId());
        orderTo.setNum(num);
        orderTo.setPromotionSessionId(redisTo.getPromotionSessionId());
        orderTo.setSkuId(redisTo.getSkuId());
        orderTo.setSeckillPrice(redisTo.getSeckillPrice());
        rabbitTemplate.convertAndSend("order-event-exchange", "order.seckill.order", orderTo);
        return orderSn;
    }

    /**
     * 保存秒杀场次的信息
     * @param list
     */
    private void saveSessionInfos(List<SeckillSessionTo> list) {
        list.forEach(session -> {
            long startTime = session.getStartTime().getTime();
            long endTime = session.getEndTime().getTime();
            String key = SESSIONS_CACHE_PREFIX + startTime +"_" + endTime;
            Boolean hasKey = stringRedisTemplate.hasKey(key);
            assert hasKey != null;
            // 缓存活动信息
            if (!hasKey) {
                List<String> collect = session.getRelationSkus().stream().map(item-> item.getPromotionSessionId() + "-"  + item.getSkuId()).collect(Collectors.toList());
                stringRedisTemplate.opsForList().leftPushAll(key, collect);
            }
        });
    }

    /**
     * 保存秒杀场次的 商品信息
     * @param sessions
     */
    private void saveSessionSkuInfos(List<SeckillSessionTo> sessions) {
        sessions.forEach(session ->{
            // 准备hash操作
            BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
            session.getRelationSkus().forEach(relationTo -> {
                // 验证场次 商品信息 是否存在
                String redisKey = relationTo.getPromotionSessionId() + "-" + relationTo.getSkuId();
                Boolean b = ops.hasKey(redisKey);
                assert b != null;
                if(!b) {
                    //缓存商品
                    SeckillSkuRedisTo redisTo = new SeckillSkuRedisTo();
                    // sku的基本信息
                    Result<SkuInfoTo> r = productFeignService.get(relationTo.getSkuId());
                    if (r.success()) {
                        redisTo.setSkuInfoTo(r.getData());
                    }
                    // 商品的秒杀信息
                    BeanUtils.copyProperties(relationTo, redisTo);

                    // 设置当前商品的秒杀时间信息
                    redisTo.setStartTime(session.getStartTime().getTime());
                    redisTo.setEndTime(session.getEndTime().getTime());

                    // 随机码
                    String token = UUID.randomUUID().toString().replace("-", "");
                    redisTo.setRandomCode(token);
                    ops.put(relationTo.getPromotionSessionId() + "-" + relationTo.getSkuId(), JSON.toJSONString(redisTo));

                    // 引入分布式信号量
                    RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + token);
                    semaphore.trySetPermits(relationTo.getSeckillCount().intValue());
                }
            });
        });
    }
}
