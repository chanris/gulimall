package com.chanris.gulimall.member.feign;

import com.chanris.gulimall.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author chenyue7@foxmail.com
 * @date 27/1/2024
 * @description 远程调用接口
 * 1. 引入 openfeign，loadbalancer
 * 2. 写一个feign接口
 * 3. application入口 开启远程调用功能 @EnableFeignClients
 */
@FeignClient("gulimall-coupon") // 远端服务名称
@Service
public interface CouponFeignService {

    @RequestMapping("/coupon/coupon/member/list")
    public Result memebercoupons();
}
