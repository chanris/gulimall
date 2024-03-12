package com.chanris.gulimall.order.feign;

import com.chanris.gulimall.common.to.product.SpuInfoTo;
import com.chanris.gulimall.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chenyue7@foxmail.com
 * @date 12/3/2024
 * @description
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {

    @GetMapping("product/spuinfo/skuId/{id}")
    @ResponseBody
    Result<SpuInfoTo> getSpuInfoBySkuId(@PathVariable("id") Long id);
}
