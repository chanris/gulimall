package com.chanris.gulimall.order.config;

import com.chanris.gulimall.order.interceptor.LoginUserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author chenyue7@foxmail.com
 * @date 11/3/2024
 * @description
 */
@Configuration
public class OrderWebConfiguration implements WebMvcConfigurer {
    @Resource
    LoginUserInterceptor loginUserInterceptor;

    // 设置请求拦截规则
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginUserInterceptor).addPathPatterns("/**"); //  全部拦截
    }
}
