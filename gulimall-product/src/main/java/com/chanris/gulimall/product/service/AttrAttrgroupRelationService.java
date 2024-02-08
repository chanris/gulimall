package com.chanris.gulimall.product.service;

import com.chanris.gulimall.common.service.CrudService;
import com.chanris.gulimall.product.dto.AttrAttrgroupRelationDTO;
import com.chanris.gulimall.product.entity.AttrAttrgroupRelationEntity;

import java.util.List;

/**
 * 属性&属性分组关联
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
public interface AttrAttrgroupRelationService extends CrudService<AttrAttrgroupRelationEntity, AttrAttrgroupRelationDTO> {

    void deleteBatchByAttrGroupId(List<Long> attrGroupIds);
}