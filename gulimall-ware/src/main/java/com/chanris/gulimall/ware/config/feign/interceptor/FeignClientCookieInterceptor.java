package com.chanris.gulimall.ware.config.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chenyue7@foxmail.com
 * @date 31/5/2024
 * @description
 */
@Slf4j
@Component
public class FeignClientCookieInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        log.info("远程调用地址： {}", template.url());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) return;
        HttpServletRequest request = attributes.getRequest();
        template.header("Cookie", request.getHeader("Cookie"));
        log.info("请求头信息：{}", template.headers().toString());
    }
}
