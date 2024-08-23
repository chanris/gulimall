package com.chanris.gulimall.cart.config.interceptor;

import com.chanris.gulimall.cart.interceptor.CartInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author chenyue7@foxmail.com
 * @date 9/3/2024
 * @description 注册拦截器
 */
@Configuration
public class GulimallMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CartInterceptor()).addPathPatterns("/**");
    }
}
