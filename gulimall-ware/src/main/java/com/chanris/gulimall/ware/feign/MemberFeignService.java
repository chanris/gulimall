package com.chanris.gulimall.ware.feign;

import com.chanris.gulimall.common.to.member.MemberReceiveAddressTo;
import com.chanris.gulimall.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author chenyue7@foxmail.com
 * @date 12/3/2024
 * @description
 */
@FeignClient("gulimall-member")
public interface MemberFeignService {
    @GetMapping("member/memberreceiveaddress/{id}")
    Result<MemberReceiveAddressTo> get(@PathVariable("id") Long id);
}
