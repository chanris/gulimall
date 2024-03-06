package com.chanris.gulimall.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chanris.gulimall.common.service.CrudService;
import com.chanris.gulimall.product.dto.AttrDTO;
import com.chanris.gulimall.product.dto.AttrGroupDTO;
import com.chanris.gulimall.product.entity.AttrGroupEntity;
import com.chanris.gulimall.product.vo.SpuItemAttrGroupVo;
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

    List<AttrDTO> attrListWithoutRelation(Long attrgroupId);

    List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId);
}