package com.chanris.gulimall.seckill.scheduled;

import com.alibaba.fastjson2.JSON;
import com.chanris.gulimall.common.to.seckill.SeckillSessionTo;
import com.chanris.gulimall.common.to.seckill.SeckillSkuRedisTo;
import com.chanris.gulimall.common.to.seckill.SkuInfoTo;
import com.chanris.gulimall.common.utils.Result;
import com.chanris.gulimall.seckill.feign.CouponFeignService;
import com.chanris.gulimall.seckill.feign.ProductFeignService;
import com.chanris.gulimall.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/12
 * @description 秒杀商品
 *  每天晚上三点：上架最近三天需要秒杀的商品
 */
@Slf4j
@Component
@EnableAsync
@EnableScheduling
public class SeckillSKuScheduled {

    @Resource
    private SeckillService seckillService;

    @Resource
    private RedissonClient redissonClient;

    private final String upload_lock = "seckill:upload:lock";

    @Scheduled(cron = "*/3 * * * * ?")
    public void uploadSeckillSkuLatest3Days() {
        log.info("上架秒杀商品信息...");
        //分布式锁
        RLock lock = redissonClient.getLock(upload_lock);
        lock.lock(10, TimeUnit.SECONDS);
        try {
            seckillService.uploadSeckillSkuLatest3Days();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
