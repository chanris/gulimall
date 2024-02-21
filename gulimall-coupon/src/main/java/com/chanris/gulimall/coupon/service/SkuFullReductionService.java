package com.chanris.gulimall.coupon.service;

import com.chanris.gulimall.common.service.CrudService;
import com.chanris.gulimall.common.to.SkuReductionTo;
import com.chanris.gulimall.coupon.dto.SkuFullReductionDTO;
import com.chanris.gulimall.coupon.entity.SkuFullReductionEntity;

/**
 * 商品满减信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
public interface SkuFullReductionService extends CrudService<SkuFullReductionEntity, SkuFullReductionDTO> {

    void saveSkuReduction(SkuReductionTo skuReductionTo);
}