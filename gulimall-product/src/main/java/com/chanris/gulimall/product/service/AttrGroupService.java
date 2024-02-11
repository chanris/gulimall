package com.chanris.gulimall.product.service;

import com.chanris.gulimall.common.service.CrudService;
import com.chanris.gulimall.product.dto.AttrGroupDTO;
import com.chanris.gulimall.product.entity.AttrGroupEntity;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * 属性分组
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
public interface AttrGroupService extends CrudService<AttrGroupEntity, AttrGroupDTO> {

    void deleteWithRelation(Long[] attrGroupIds);

    List<AttrGroupDTO> attrGroupWithAttr(Long catelogId);
}