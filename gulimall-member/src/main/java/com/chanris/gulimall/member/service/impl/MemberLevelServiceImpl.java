package com.chanris.gulimall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.member.service.MemberLevelService;
import com.chanris.gulimall.member.dao.MemberLevelDao;
import com.chanris.gulimall.member.dto.MemberLevelDTO;
import com.chanris.gulimall.member.entity.MemberLevelEntity;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 会员等级
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class MemberLevelServiceImpl extends CrudServiceImpl<MemberLevelDao, MemberLevelEntity, MemberLevelDTO> implements MemberLevelService {

    @Override
    public QueryWrapper<MemberLevelEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<MemberLevelEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}