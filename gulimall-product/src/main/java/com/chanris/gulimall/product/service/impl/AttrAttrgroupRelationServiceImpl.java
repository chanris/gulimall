package com.chanris.gulimall.product.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.common.utils.ObjectConvert;
import com.chanris.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.chanris.gulimall.product.dto.AttrAttrgroupRelationDTO;
import com.chanris.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.chanris.gulimall.product.service.AttrAttrgroupRelationService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class AttrAttrgroupRelationServiceImpl extends CrudServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity, AttrAttrgroupRelationDTO> implements AttrAttrgroupRelationService {

    @Resource
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Override
    public QueryWrapper<AttrAttrgroupRelationEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");
        Long attrGroupId = ObjectConvert.toLong(params.get("attrGroupId"));
        QueryWrapper<AttrAttrgroupRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);
        wrapper.eq(ObjectUtil.isNotNull(attrGroupId), "attr_group_id", attrGroupId);
        return wrapper;
    }

    @Override
    public void deleteBatchByAttrGroupId(List<Long> attrGroupIds) {

    }
}