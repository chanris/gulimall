package com.chanris.gulimall.product.feign;

import com.chanris.gulimall.common.to.SkuReductionTo;
import com.chanris.gulimall.common.to.SpuBoundTo;
import com.chanris.gulimall.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author chenyue7@foxmail.com
 * @date 21/2/2024
 * @description
 */
@Service
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    /**
     * 1. CouponFeignService.saveSpuBounds(spuBoundTo)
     *      1) @RequestBody 将这个对象转为json
     *      2) 找到gulimall-coupon服务，给/coupon/spubounds/save发送请求
     *         将上一步转的json放在请求体位置，发送请求。
     *      3) 对方服务收到请求。 请求体里有json数据
     *         (@RequestBody SpuBoundsEntity spuBounds); 将请求体的json转为SpuBoundsEntity;
     * 只要json数据模型是兼容的。双方服务无需使用同一个to
     * @param spuBoundTo
     * @return
     */
    @PostMapping("/coupon/spubounds")
    Result saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("/coupon/skufullreduction/saveinfo")
    Result saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
