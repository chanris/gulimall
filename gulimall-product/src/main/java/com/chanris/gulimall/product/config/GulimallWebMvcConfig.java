package com.chanris.gulimall.product.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/6
 * @description
 */
@Configuration
public class GulimallWebMvcConfig implements WebMvcConfigurer {

    /**
     * 跨域设置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://order.gulimall.com") // 运行来order.gulimall.com
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true); //运行携带cookie
    }
}
