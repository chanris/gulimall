package com.chanris.gulimall.product.dao;

import com.chanris.gulimall.common.dao.BaseDao;
import com.chanris.gulimall.product.dto.AttrGroupDTO;
import com.chanris.gulimall.product.entity.AttrGroupEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * 属性分组
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Mapper
public interface AttrGroupDao extends BaseDao<AttrGroupEntity> {

    List<AttrGroupDTO> getAttrGroupListWithAttr(@Nullable  @Param("catelogId") Long catelogId);
}