package com.chanris.gulimall.seckill.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenyue7@foxmail.com
 * @date 29/2/2024
 * @description redisson 分布式锁配置
 */
@Configuration
public class RedissonConfig {

    @Bean
    RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.125.129:6379").setPassword("acid10837");
        return Redisson.create(config);
    }
}
