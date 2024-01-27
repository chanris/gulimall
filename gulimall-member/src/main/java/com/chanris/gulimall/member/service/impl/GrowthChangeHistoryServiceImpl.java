package com.chanris.gulimall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.member.service.GrowthChangeHistoryService;
import com.chanris.gulimall.member.dao.GrowthChangeHistoryDao;
import com.chanris.gulimall.member.dto.GrowthChangeHistoryDTO;
import com.chanris.gulimall.member.entity.GrowthChangeHistoryEntity;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 成长值变化历史记录
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class GrowthChangeHistoryServiceImpl extends CrudServiceImpl<GrowthChangeHistoryDao, GrowthChangeHistoryEntity, GrowthChangeHistoryDTO> implements GrowthChangeHistoryService {

    @Override
    public QueryWrapper<GrowthChangeHistoryEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<GrowthChangeHistoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}