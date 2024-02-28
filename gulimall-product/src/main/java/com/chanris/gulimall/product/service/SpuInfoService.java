package com.chanris.gulimall.product.service;

import com.chanris.gulimall.common.service.CrudService;
import com.chanris.gulimall.product.dto.SpuInfoDTO;
import com.chanris.gulimall.product.entity.SpuInfoEntity;
import com.chanris.gulimall.product.vo.SpuSaveVo;

/**
 * spu信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
public interface SpuInfoService extends CrudService<SpuInfoEntity, SpuInfoDTO> {
    void savesupInfo(SpuSaveVo vo);

    void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity);

    void up(Long spuId);
}