package com.chanris.gulimall.product.feign;

import com.chanris.gulimall.common.exception.CodeEnum;
import com.chanris.gulimall.common.to.es.SkuEsModel;
import com.chanris.gulimall.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 28/2/2024
 * @description
 */
@Service
@FeignClient("gulimall-search")
public interface SearchFeignService {
    @PostMapping("search/save/product")
    Result<?> productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);
}
