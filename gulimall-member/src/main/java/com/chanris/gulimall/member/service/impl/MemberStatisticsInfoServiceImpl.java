package com.chanris.gulimall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.member.service.MemberStatisticsInfoService;
import com.chanris.gulimall.member.dao.MemberStatisticsInfoDao;
import com.chanris.gulimall.member.dto.MemberStatisticsInfoDTO;
import com.chanris.gulimall.member.entity.MemberStatisticsInfoEntity;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 会员统计信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class MemberStatisticsInfoServiceImpl extends CrudServiceImpl<MemberStatisticsInfoDao, MemberStatisticsInfoEntity, MemberStatisticsInfoDTO> implements MemberStatisticsInfoService {

    @Override
    public QueryWrapper<MemberStatisticsInfoEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<MemberStatisticsInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}