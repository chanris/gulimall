package com.chanris.gulimall.product.dao;

import com.chanris.gulimall.common.dao.BaseDao;
import com.chanris.gulimall.product.entity.CategoryEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Mapper
public interface CategoryDao extends BaseDao<CategoryEntity> {

}