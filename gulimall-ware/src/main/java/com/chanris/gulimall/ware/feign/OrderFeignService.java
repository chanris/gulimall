package com.chanris.gulimall.ware.feign;

import com.chanris.gulimall.common.to.OrderTo;
import com.chanris.gulimall.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chenyue7@foxmail.com
 * @date 17/3/2024
 * @description
 */
@FeignClient("gulimall-order")
public interface OrderFeignService {
    @GetMapping("/status/{orderSn}")
    @ResponseBody
    Result<OrderTo> getOrderStatus(@PathVariable("orderSn") String orderSn);
}
