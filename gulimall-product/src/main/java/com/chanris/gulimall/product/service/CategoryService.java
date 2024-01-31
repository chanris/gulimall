package com.chanris.gulimall.product.service;

import com.chanris.gulimall.common.service.CrudService;
import com.chanris.gulimall.product.dto.CategoryDTO;
import com.chanris.gulimall.product.entity.CategoryEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品三级分类
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
public interface CategoryService extends CrudService<CategoryEntity, CategoryDTO> {

    List<CategoryDTO> listWithTree();
}