package com.chanris.gulimall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.member.service.MemberReceiveAddressService;
import com.chanris.gulimall.member.dao.MemberReceiveAddressDao;
import com.chanris.gulimall.member.dto.MemberReceiveAddressDTO;
import com.chanris.gulimall.member.entity.MemberReceiveAddressEntity;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 会员收货地址
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class MemberReceiveAddressServiceImpl extends CrudServiceImpl<MemberReceiveAddressDao, MemberReceiveAddressEntity, MemberReceiveAddressDTO> implements MemberReceiveAddressService {

    @Resource
    private MemberReceiveAddressDao memberReceiveAddressDao;
    @Override
    public QueryWrapper<MemberReceiveAddressEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<MemberReceiveAddressEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public List<MemberReceiveAddressEntity> getAddress(Long memberId) {
        return memberReceiveAddressDao.selectList(new QueryWrapper<MemberReceiveAddressEntity>().eq("member_id", memberId));
    }
}