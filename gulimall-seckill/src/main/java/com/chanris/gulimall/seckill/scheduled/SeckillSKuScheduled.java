package com.chanris.gulimall.seckill.scheduled;

import com.chanris.gulimall.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/12
 * @description 秒杀商品
 *  每天晚上三点：上架最近三天需要秒杀的商品
 */
@Slf4j
@Component
@EnableAsync // 启动异步任务
@EnableScheduling
public class SeckillSKuScheduled {

    @Resource
    private SeckillService seckillService;
    @Resource
    private RedissonClient redissonClient;

    private final String upload_lock = "seckill:upload:lock";

    @Scheduled(cron = "* * 3 * * ?") // 秒 分 时 日期 月份 年： 每天凌晨三点整 执行一次
    public void uploadSeckillSkuLatest3Days() {
        log.info("上架秒杀商品信息...");
        //分布式锁
        RLock lock = redissonClient.getLock(upload_lock);
        try {
            lock.lock(10, TimeUnit.SECONDS);
            seckillService.uploadSeckillSkuLatest3Days();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
