package com.chanris.gulimall.seckill.feign;

import com.chanris.gulimall.common.to.seckill.SkuInfoTo;
import com.chanris.gulimall.common.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/12
 * @description
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {
    /**
     * 远程获得商品基本信息
     * @param id
     * @return
     */
    @GetMapping("product/skuinfo/{id}")
    Result<SkuInfoTo> get(@PathVariable("id") Long id);
}
