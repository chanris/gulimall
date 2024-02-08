package com.chanris.gulimall.product.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.common.utils.ObjectConvert;
import com.chanris.gulimall.product.dao.CategoryBrandRelationDao;
import com.chanris.gulimall.product.dto.CategoryBrandRelationDTO;
import com.chanris.gulimall.product.entity.CategoryBrandRelationEntity;
import com.chanris.gulimall.product.service.CategoryBrandRelationService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class CategoryBrandRelationServiceImpl extends CrudServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity, CategoryBrandRelationDTO> implements CategoryBrandRelationService {

    @Override
    public QueryWrapper<CategoryBrandRelationEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");
        Long brandId = ObjectConvert.toLong(params.get("brandId"));
        Long catelogId = ObjectConvert.toLong(params.get("catelogId"));
        QueryWrapper<CategoryBrandRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);
        wrapper.eq(ObjectUtil.isNotNull(brandId), "brand_id", brandId);
        wrapper.eq(ObjectUtil.isNotNull(catelogId), "catelog_id", catelogId);
        return wrapper;
    }


}