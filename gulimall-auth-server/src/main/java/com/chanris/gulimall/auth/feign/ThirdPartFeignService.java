package com.chanris.gulimall.auth.feign;

import com.chanris.gulimall.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author chenyue7@foxmail.com
 * @date 6/3/2024
 * @description
 */
@Service
@FeignClient("gulimall-third-party")
public interface ThirdPartFeignService {

    @GetMapping(value = "/sms/sendCode")
    Result<?> sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code);

}
