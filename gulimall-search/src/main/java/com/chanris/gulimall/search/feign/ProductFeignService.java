package com.chanris.gulimall.search.feign;

import com.chanris.gulimall.common.to.product.AttrTo;
import com.chanris.gulimall.common.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author chenyue7@foxmail.com
 * @date 5/3/2024
 * @description
 */
@Service
@FeignClient("gulimall-product")
public interface ProductFeignService {
    @GetMapping("product/attr/{id}")
    Result<AttrTo> get(@PathVariable("id") Long id);
}
