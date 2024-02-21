package com.chanris.gulimall.product.service;

import com.chanris.gulimall.common.service.CrudService;
import com.chanris.gulimall.product.dto.SpuInfoDescDTO;
import com.chanris.gulimall.product.entity.SpuInfoDescEntity;

/**
 * spu信息介绍
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
public interface SpuInfoDescService extends CrudService<SpuInfoDescEntity, SpuInfoDescDTO> {

    void saveSpuInfoDesc(SpuInfoDescEntity spuInfoDescEntity);
}