package com.chanris.gulimall.member.service;

import com.chanris.gulimall.common.service.CrudService;
import com.chanris.gulimall.member.dto.MemberReceiveAddressDTO;
import com.chanris.gulimall.member.entity.MemberReceiveAddressEntity;

import java.util.List;

/**
 * 会员收货地址
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
public interface MemberReceiveAddressService extends CrudService<MemberReceiveAddressEntity, MemberReceiveAddressDTO> {

    List<MemberReceiveAddressEntity> getAddress(Long memberId);
}