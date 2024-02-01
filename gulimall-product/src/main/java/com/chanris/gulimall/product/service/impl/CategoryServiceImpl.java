package com.chanris.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.product.dao.CategoryDao;
import com.chanris.gulimall.product.dto.CategoryDTO;
import com.chanris.gulimall.product.entity.CategoryEntity;
import com.chanris.gulimall.product.service.CategoryService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品三级分类
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class CategoryServiceImpl extends CrudServiceImpl<CategoryDao, CategoryEntity, CategoryDTO> implements CategoryService {

    @Resource
    private CategoryDao categoryDao;

    @Override
    public QueryWrapper<CategoryEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<CategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public List<CategoryDTO> listWithTree() {
        //1. 查出所有分类
        List<CategoryDTO> dtos = this.list(new HashMap<>(0));
        //2. 组装成树结构
        List<CategoryDTO> level1Menus = dtos.stream().filter( menu -> menu.getParentCid() == 0).map(menu -> {
            menu.setChildren(getChildren(menu, dtos));
            return menu;
        }).sorted(Comparator.comparingInt(CategoryDTO::getSort)).collect(Collectors.toList());
        return level1Menus;
    }

    private List<CategoryDTO> getChildren(CategoryDTO root, List<CategoryDTO> all) {
        List<CategoryDTO> menus = all.stream().filter(dto -> dto.getParentCid().equals(root.getCatId())).map(dto -> {
            // 递归找到所有的分类的子分类列表
            dto.setChildren(getChildren(dto, all));
            return dto;
        }).sorted(Comparator.comparingInt(CategoryDTO::getSort)).collect(Collectors.toList());
        return menus;
    }
}