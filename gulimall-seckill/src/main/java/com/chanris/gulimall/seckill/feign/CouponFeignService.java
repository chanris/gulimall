package com.chanris.gulimall.seckill.feign;

import com.chanris.gulimall.common.to.seckill.SeckillSessionTo;
import com.chanris.gulimall.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/12
 * @description
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {
    @GetMapping("coupon/seckillsession/latest3DaysSession")
    Result<List<SeckillSessionTo>> getLatest3DaysSession();
}
