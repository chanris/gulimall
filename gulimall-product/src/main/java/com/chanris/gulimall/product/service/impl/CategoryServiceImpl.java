package com.chanris.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.product.dao.CategoryDao;
import com.chanris.gulimall.product.dto.CategoryDTO;
import com.chanris.gulimall.product.entity.CategoryEntity;
import com.chanris.gulimall.product.service.CategoryService;
import cn.hutool.core.util.StrUtil;
import com.chanris.gulimall.product.vo.Catelog2Vo;
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

    @Override
    public List<CategoryEntity> getTopLevelCategoryList() {
        QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_cid", 0L);
        return  categoryDao.selectList(queryWrapper);
    }

    @Override
    public Map<String, List<Catelog2Vo>> getCatalogView() {

        /*
        * 业务优化
        * 1. 将数据的多次查询变为一次
        * */
        List<CategoryEntity> categoryEntities = categoryDao.selectList(null);

        List<CategoryEntity> categoryEntityList = getParent_cid(categoryEntities, 0L);

        // 1.查出所有一级分类
        //List<CategoryEntity> categoryEntityList = this.getTopLevelCategoryList();

        // 2.封装数据
        Map<String, List<Catelog2Vo>> map = categoryEntityList.stream().collect(Collectors.toMap(k-> k.getCatId().toString(), v -> {
//            List<CategoryEntity> entities = categoryDao.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));
            List<CategoryEntity> entities = getParent_cid(categoryEntities, v.getCatId());
            List<Catelog2Vo> parentCid = null;
            if (entities != null) {
                parentCid = entities.stream().map(item -> {
                    Catelog2Vo vo = new Catelog2Vo(v.getCatId().toString(), null, "" + item.getCatId(), item.getName());
                    // 给当前二级分类找三级分类list
                    //List<CategoryEntity> level3Catalog = categoryDao.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", item.getCatId()));
                    List<CategoryEntity> level3Catalog = getParent_cid(categoryEntities, item.getCatId());
                    if (level3Catalog.size() != 0) {
                        List<Catelog2Vo.Category3Vo> vos = level3Catalog.stream().map(c3 -> {
                            Catelog2Vo.Category3Vo category3Vo = new Catelog2Vo.Category3Vo("" + item.getCatId(), "" + c3.getCatId(), c3.getName());
                            return category3Vo;
                        }).toList();
                        vo.setCatalog3List(vos);
                    }
                    return vo;
                }).toList();
            }else {
                parentCid = new ArrayList<>(0);
            }
            return parentCid;
        }));
        return null;
    }

    private List<CategoryEntity> getParent_cid(List<CategoryEntity> entities, Long cid) {
        List<CategoryEntity> result = null;
        if (entities == null || entities.size() == 0)  {
            result = new ArrayList<>(0);
            return result;
        }
        result = entities.stream().filter(item -> item.getParentCid().equals(cid)).toList();
        return result;
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