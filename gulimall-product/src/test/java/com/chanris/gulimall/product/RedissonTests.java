package com.chanris.gulimall.product;

import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

/**
 * @author chenyue7@foxmail.com
 * @date 29/2/2024
 * @description
 */
@SpringBootTest
public class RedissonTests {

    @Resource
    RedissonClient redissonClient;

    @Test
    void test01() {
        System.out.println(redissonClient);
    }
}
