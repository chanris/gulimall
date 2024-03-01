package com.chanris.gulimall.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * Spring Cache的使用
 * @Cacheable 触发将数据保存到缓存的操作
 * @CacheEvict 触发将数据从缓存删除的操作
 * @CachePut 不影响方法执行更新
 * @Caching 组合以上多个操作
 * @CacheConfig 在类级别共享缓存的相同
 * 1). 开启缓存功能 @EnableCaching
 * 2). 使用注解就能完成缓存操作
 * 4. 原理
 *   CacheAutoConfiguration -> RedisCacheConfiguration
 *   自动配置了 RedisCacheManager  -> 初始化所有的缓存
 *   ->
 */
// @EnableCaching // 使用 Spring Cache
@EnableDiscoveryClient // 开启服务注册与发现
@EnableFeignClients(basePackages = {"com.chanris.gulimall.product.feign"})   // 开启远程调用功能
@SpringBootApplication
public class GulimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class, args);
    }

}
