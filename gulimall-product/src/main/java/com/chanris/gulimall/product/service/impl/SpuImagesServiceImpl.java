package com.chanris.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.product.dao.SpuImagesDao;
import com.chanris.gulimall.product.dto.SpuImagesDTO;
import com.chanris.gulimall.product.entity.SpuImagesEntity;
import com.chanris.gulimall.product.service.SpuImagesService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * spu图片
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class SpuImagesServiceImpl extends CrudServiceImpl<SpuImagesDao, SpuImagesEntity, SpuImagesDTO> implements SpuImagesService {

    @Override
    public QueryWrapper<SpuImagesEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SpuImagesEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public void saveImages(Long id, List<String> images) {
        if(images == null || images.size() == 0) return;

        List<SpuImagesEntity> collect = images.stream().map(img -> {
            SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
            spuImagesEntity.setSpuId(id);
            spuImagesEntity.setImgUrl(img);
            return spuImagesEntity;
        }).toList();
        // 批量新增图片
        this.insertBatch(collect);
    }
}