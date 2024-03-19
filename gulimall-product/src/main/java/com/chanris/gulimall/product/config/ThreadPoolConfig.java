package com.chanris.gulimall.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.*;

/**
 * @author chenyue7@foxmail.com
 * @date 6/3/2024
 * @description
 */
@Configuration
public class ThreadPoolConfig {

    @Resource
    private ThreadPoolConfigProperties properties;

    /**
     * 向spring容器注入一个线程池
     * @return
     */
    @Bean
    public ThreadPoolExecutor executor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(properties.getCoreSize(),
                properties.getMaxSize(),
                properties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardPolicy());
        return executor;
    }

    public static void main(String[] args) {
        int i = Runtime.getRuntime().availableProcessors();
        System.out.println(i);
    }
}
