package com.chanris.gulimall.order.feign;

import com.chanris.gulimall.common.to.SkuHasStockVo;
import com.chanris.gulimall.common.to.ware.FareTo;
import com.chanris.gulimall.common.to.ware.LockStockResultTo;
import com.chanris.gulimall.common.to.ware.WareSkuLockTo;
import com.chanris.gulimall.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 12/3/2024
 * @description
 */
@FeignClient("gulimall-ware")
public interface WmsFeignService {
    @PostMapping("ware/waresku/hasstock")
    Result<List<SkuHasStockVo>> getSkusHasStock(@RequestBody List<Long> skuIds);

    @GetMapping("ware/wareinfo/fare")
    Result<FareTo> getFare(@RequestParam("addrId") Long addrId);

    @PostMapping("ware/waresku/lock/order")
    Result<?> orderLockStock(@RequestBody WareSkuLockTo vo);
}
