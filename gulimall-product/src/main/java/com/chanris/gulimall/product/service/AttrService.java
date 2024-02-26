package com.chanris.gulimall.product.service;

import com.chanris.gulimall.common.page.PageData;
import com.chanris.gulimall.common.service.CrudService;
import com.chanris.gulimall.product.dto.AttrDTO;
import com.chanris.gulimall.product.dto.ProductAttrValueDTO;
import com.chanris.gulimall.product.entity.AttrEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
public interface AttrService extends CrudService<AttrEntity, AttrDTO> {

    void saveWithAttrGroupRelation(AttrDTO attrDTO);

    void updateWithAttrGroupRelation(AttrDTO attrDTO);

    PageData<AttrDTO> pageWithOtherInfo(Map<String, Object> params);

    AttrDTO getWithAttrGroupId(Long attrId);

    void deleteWithRelation(Long[] attrIds);

    List<ProductAttrValueDTO> listforspu(Long spuId);
}