package com.chanris.gulimall.order.feign;

import com.chanris.gulimall.order.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 11/3/2024
 * @description
 */
@FeignClient("gulimall-cart")
public interface CartFeignService {
    @GetMapping(value = "/currentUserCartItems")
    List<OrderItemVo> getCurrentCartItems();
}
