package com.chanris.gulimall.product.feign;

import com.chanris.gulimall.common.to.seckill.SeckillSkuRedisTo;
import com.chanris.gulimall.common.utils.Result;
import com.chanris.gulimall.product.fallback.SeckillFeignServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/13
 * @description
 */
@Service
@FeignClient(value = "gulimall-seckill", fallback = SeckillFeignServiceFallBack.class)
public interface SeckillFeignService {
    @GetMapping("/sku/seckill/{skuId}")
    Result<SeckillSkuRedisTo> getSkuSeckillInfo(@PathVariable("skuId") Long skuId);
}
