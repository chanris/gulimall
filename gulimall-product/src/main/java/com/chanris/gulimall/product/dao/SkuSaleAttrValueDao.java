package com.chanris.gulimall.product.dao;

import com.chanris.gulimall.common.dao.BaseDao;
import com.chanris.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.chanris.gulimall.product.vo.SkuItemSaleAttrVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * sku销售属性&值
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseDao<SkuSaleAttrValueEntity> {

    List<SkuItemSaleAttrVo> getSaleAttrBySpuId(Long spuId);
}