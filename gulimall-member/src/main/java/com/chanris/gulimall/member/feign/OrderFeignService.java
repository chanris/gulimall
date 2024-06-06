package com.chanris.gulimall.member.feign;

import com.chanris.gulimall.common.page.PageData;
import com.chanris.gulimall.common.to.OrderTo;
import com.chanris.gulimall.common.utils.Result;
import com.chanris.gulimall.member.config.feign.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/2
 * @description
 */
@FeignClient(value = "gulimall-order", configuration = FeignClientConfig.class)
public interface OrderFeignService {

    /**
     * 分页查询当前登录用户的所有订单信息
     * @param params
     * @return
     */
    @PostMapping("/order/order/listWithItem")
    Result<PageData<OrderTo>> listWithItem(@RequestBody Map<String, Object> params);
}
