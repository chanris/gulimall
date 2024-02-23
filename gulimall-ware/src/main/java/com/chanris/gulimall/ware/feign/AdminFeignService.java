package com.chanris.gulimall.ware.feign;

import com.chanris.gulimall.common.to.SysUserTo;
import com.chanris.gulimall.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author chenyue7@foxmail.com
 * @date 23/2/2024
 * @description
 */
@Service
@FeignClient("gulimall-admin")
public interface AdminFeignService {
    @GetMapping("admin/sys/user/info")
    Result<SysUserTo> info(@RequestHeader(name = "Token",required = true) String token);
}
