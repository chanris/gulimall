package com.chanris.gulimall.member.service;

import com.chanris.gulimall.common.service.CrudService;
import com.chanris.gulimall.member.dto.MemberDTO;
import com.chanris.gulimall.member.entity.MemberEntity;
import com.chanris.gulimall.member.vo.MemberRegistVo;
import com.chanris.gulimall.member.vo.MemberUserLoginVo;
import com.chanris.gulimall.member.vo.SocialUser;

/**
 * 会员
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
public interface MemberService extends CrudService<MemberEntity, MemberDTO> {

    void regist(MemberRegistVo registVo);
    void checkPhoneUnique(String email);
    void checkUsernameUnique(String username);
    MemberEntity login(MemberUserLoginVo vo);

    MemberEntity login(SocialUser socialUser) throws Exception;
}