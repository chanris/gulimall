package com.chanris.gulimall.product.service;

import com.chanris.gulimall.common.service.CrudService;
import com.chanris.gulimall.product.dto.SpuImagesDTO;
import com.chanris.gulimall.product.entity.SpuImagesEntity;
import com.chanris.gulimall.product.entity.SpuInfoDescEntity;

import java.util.List;

/**
 * spu图片
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
public interface SpuImagesService extends CrudService<SpuImagesEntity, SpuImagesDTO> {
    void saveImages(Long id, List<String> images);
}