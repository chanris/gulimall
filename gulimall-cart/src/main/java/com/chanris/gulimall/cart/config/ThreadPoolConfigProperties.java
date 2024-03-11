package com.chanris.gulimall.cart.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author chenyue7@foxmail.com
 * @date 6/3/2024
 * @description
 */
@Data
@Component
@ConfigurationProperties(prefix = "gulimall.thread")
public class ThreadPoolConfigProperties {
    private Integer coreSize = 20;
    private Integer maxSize = 200;
    private Integer keepAliveTime = 10;
}
