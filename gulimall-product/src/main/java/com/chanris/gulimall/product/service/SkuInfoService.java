package com.chanris.gulimall.product.service;

import com.chanris.gulimall.common.service.CrudService;
import com.chanris.gulimall.product.dto.SkuInfoDTO;
import com.chanris.gulimall.product.entity.SkuInfoEntity;
import com.chanris.gulimall.product.vo.SkuItemVo;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * sku信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
public interface SkuInfoService extends CrudService<SkuInfoEntity, SkuInfoDTO> {

    List<SkuInfoEntity> getSkusBySpuId(Long spuId);

    SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException;
}