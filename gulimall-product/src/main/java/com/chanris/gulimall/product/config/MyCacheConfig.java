package com.chanris.gulimall.product.config;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

/**
 * @author chenyue7@foxmail.com
 * @date 1/3/2024
 * @description
 */
@EnableConfigurationProperties(CacheProperties.class)
@EnableCaching
@Configuration
public class MyCacheConfig {

    @Bean
    RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        cacheConfiguration = cacheConfiguration.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        cacheConfiguration = cacheConfiguration.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        CacheProperties.Redis redis = cacheProperties.getRedis();
        if(redis.getTimeToLive() != null) {
            cacheConfiguration = cacheConfiguration.entryTtl(redis.getTimeToLive());
        }
        if(redis.getKeyPrefix() != null) {
            cacheConfiguration = cacheConfiguration.prefixCacheNameWith(redis.getKeyPrefix());
        }
        if(!redis.isCacheNullValues()) {
            cacheConfiguration = cacheConfiguration.disableCachingNullValues();
        }
        if(!redis.isUseKeyPrefix()) {
            cacheConfiguration = cacheConfiguration.disableKeyPrefix();
        }
        return cacheConfiguration;
    }
}
