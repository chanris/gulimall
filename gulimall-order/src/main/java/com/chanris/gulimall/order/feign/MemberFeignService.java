package com.chanris.gulimall.order.feign;

import com.chanris.gulimall.order.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 11/3/2024
 * @description
 */
@FeignClient(value = "gulimall-member")
public interface MemberFeignService {
    @ResponseBody
    @GetMapping("member/memberreceiveaddress/{memberId}/addresses")
    List<MemberAddressVo> getAddress(@PathVariable("memberId") Long memberId);

}
