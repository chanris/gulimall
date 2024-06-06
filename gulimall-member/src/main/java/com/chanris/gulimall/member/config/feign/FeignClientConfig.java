package com.chanris.gulimall.member.config.feign;

import com.chanris.gulimall.member.config.feign.jackson.CustomDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/2
 * @description
 */
@Configuration
public class FeignClientConfig {
    @Bean
    public CustomDecoder customDecoder() {
        return new CustomDecoder();
    }
}
