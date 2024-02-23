package com.chanris.gulimall.ware.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.CrudService;
import com.chanris.gulimall.ware.dto.WareSkuDTO;
import com.chanris.gulimall.ware.entity.WareSkuEntity;

import java.util.List;

/**
 * 商品库存
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
public interface WareSkuService extends CrudService<WareSkuEntity, WareSkuDTO> {

    WareSkuEntity getByWrapper(QueryWrapper<WareSkuEntity> wrapper);
}