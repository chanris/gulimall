package com.chanris.gulimall.ware.feign;

import com.chanris.gulimall.common.to.product.SkuInfoTo;
import com.chanris.gulimall.common.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author chenyue7@foxmail.com
 * @date 23/2/2024
 * @description
 */
@Service
@FeignClient("gulimall-product")
public interface ProductFeignService {

    @GetMapping("product/skuinfo/{id}")
    Result<SkuInfoTo> get(@PathVariable("id") Long id);
}
