package com.chanris.gulimall.product.feign;

import com.chanris.gulimall.common.to.SkuHasStockVo;
import com.chanris.gulimall.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 27/2/2024
 * @description
 */
@Service
@FeignClient("gulimall-ware")
public interface WareFeignService {
    @PostMapping("ware/waresku/hasstock")
    Result<List<SkuHasStockVo>> getSkusHasStock(@RequestBody List<Long> skuIds);
}
