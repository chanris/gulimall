package com.chanris.gulimall.product.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.common.utils.ObjectConvert;
import com.chanris.gulimall.product.dao.SkuInfoDao;
import com.chanris.gulimall.product.dto.SkuInfoDTO;
import com.chanris.gulimall.product.entity.SkuInfoEntity;
import com.chanris.gulimall.product.service.SkuInfoService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * sku信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class SkuInfoServiceImpl extends CrudServiceImpl<SkuInfoDao, SkuInfoEntity, SkuInfoDTO> implements SkuInfoService {

    @Resource
    private SkuInfoDao skuInfoDao;

    @Override
    public QueryWrapper<SkuInfoEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");
        Long catalogId = ObjectConvert.toLong(params.get("catalogId"));
        Long brandId = ObjectConvert.toLong(params.get("brandId"));
        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);
        wrapper.eq(ObjectUtil.isNotNull(catalogId), "catalog_id", catalogId);
        wrapper.eq(ObjectUtil.isNotNull(brandId), "brand_id", brandId);
        return wrapper;
    }

    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {
        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spu_id", spuId);
        return skuInfoDao.selectList(queryWrapper);
    }
}