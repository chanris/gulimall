package com.chanris.gulimall.auth.feign;

import com.chanris.gulimall.auth.vo.SocialUser;
import com.chanris.gulimall.auth.vo.UserLoginVo;
import com.chanris.gulimall.auth.vo.UserRegisterVo;
import com.chanris.gulimall.common.utils.Result;
import com.chanris.gulimall.common.vo.MemberResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author chenyue7@foxmail.com
 * @date 6/3/2024
 * @description
 */
@Service
@FeignClient("gulimall-member")
public interface MemberFeignService {

    @PostMapping(value = "/member/member/register")
    Result<?> register(@RequestBody UserRegisterVo vo);
//
    @PostMapping(value = "/member/member/login")
    Result<MemberResponseVo> login(@RequestBody UserLoginVo vo);

    @PostMapping(value = "/member/member/oauth2/login")
    Result<MemberResponseVo> oauthLogin(@RequestBody SocialUser socialUser) throws Exception;

    @PostMapping(value = "/member/member/weixin/login")
    Result<?> weixinLogin(@RequestParam("accessTokenInfo") String accessTokenInfo);
}

