package com.chanris.gulimall.product.service;

import com.chanris.gulimall.common.service.CrudService;
import com.chanris.gulimall.product.dto.SkuSaleAttrValueDTO;
import com.chanris.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.chanris.gulimall.product.vo.SkuItemSaleAttrVo;

import java.util.List;

/**
 * sku销售属性&值
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
public interface SkuSaleAttrValueService extends CrudService<SkuSaleAttrValueEntity, SkuSaleAttrValueDTO> {

    List<SkuItemSaleAttrVo> getSaleAttrBySpuId(Long spuId);

    List<String> getSkuSaleAttrValuesAsStringList(Long skuId);
}