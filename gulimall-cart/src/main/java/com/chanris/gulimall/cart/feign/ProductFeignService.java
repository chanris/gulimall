package com.chanris.gulimall.cart.feign;

import com.chanris.gulimall.common.to.product.SkuInfoTo;
import com.chanris.gulimall.common.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 9/3/2024
 * @description
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {
    @GetMapping("{id}")
    Result<SkuInfoTo> get(@PathVariable("id") Long id);

    @GetMapping("product/skusaleattrvalue/stringlist/{skuId}")
    List<String> getSkuSaleAttrValues(@PathVariable("skuId") Long skuId);

    /**
     * 根据skuId查询当前商品的最新价格
     * @param skuId
     * @return
     */
    @GetMapping(value = "/product/skuinfo/{skuId}/price")
    BigDecimal getPrice(@PathVariable("skuId") Long skuId);
}
