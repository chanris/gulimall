package com.chanris.gulimall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenyue7@foxmail.com
 * @date 29/2/2024
 * @description
 */
@Configuration
public class RedissonConfig {

    @Bean
    RedissonClient redissonClient() {
        Config config = new Config();
//        config.useClusterServers()
//                // use "rediss://" for SSL connection
//                .addNodeAddress("perredis://192.168.125.129:6379");
        config.useSingleServer().setAddress("redis://192.168.125.129:6379").setPassword("acid10837");
        RedissonClient redisson = Redisson.create(config);
        return  redisson;
    }
}
