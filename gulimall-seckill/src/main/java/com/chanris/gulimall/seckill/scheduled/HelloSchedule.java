package com.chanris.gulimall.seckill.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/12
 * @description
 */
@Slf4j
@Component
@EnableScheduling
@EnableAsync
public class HelloSchedule {

    /**
     * 定时任务不应该阻塞。默认是阻塞的
     */
   /* @Async
    @Scheduled(cron = "* * * * * ?")
    public  void hello() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        log.info("hello...");
    }*/
}
