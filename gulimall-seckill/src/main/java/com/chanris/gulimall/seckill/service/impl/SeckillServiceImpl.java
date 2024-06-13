package com.chanris.gulimall.seckill.service.impl;

import com.alibaba.fastjson2.JSON;
import com.chanris.gulimall.common.to.seckill.SeckillSessionTo;
import com.chanris.gulimall.common.to.seckill.SeckillSkuRedisTo;
import com.chanris.gulimall.common.to.seckill.SkuInfoTo;
import com.chanris.gulimall.common.utils.Result;
import com.chanris.gulimall.seckill.feign.CouponFeignService;
import com.chanris.gulimall.seckill.feign.ProductFeignService;
import com.chanris.gulimall.seckill.service.SeckillService;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/12
 * @description
 */
@Service
public class SeckillServiceImpl implements SeckillService {
    @Resource
    private CouponFeignService couponFeignService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    RedissonClient redissonClient;
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
     * 保存秒杀场次的信息
     * @param list
     */
    private void saveSessionInfos(List<SeckillSessionTo> list) {
        list.stream().forEach(session -> {
            long startTime = session.getStartTime().getTime();
            long endTime = session.getEndTime().getTime();
            String key = SESSIONS_CACHE_PREFIX + startTime +"_" + endTime;
            Boolean hasKey = stringRedisTemplate.hasKey(key);
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
        sessions.stream().forEach(session ->{
            // 准备hash操作
            BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
            session.getRelationSkus().forEach(relationTo -> {
                // 验证场次 商品信息 是否存在
                String redisKey = relationTo.getPromotionSessionId() + "-" + relationTo.getSkuId();
                if(!ops.hasKey(redisKey)) {
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
