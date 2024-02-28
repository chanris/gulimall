package com.chanris.gulimall.product.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chanris.gulimall.common.dao.BaseDao;
import com.chanris.gulimall.product.dto.AttrDTO;
import com.chanris.gulimall.product.entity.AttrEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Mapper
public interface AttrDao extends BaseDao<AttrEntity> {

    IPage<AttrDTO> pageWithOtherInfo(IPage<?> page, @Param("catelogId") Long catelogId, @Param("attrType") Integer attrType);

    AttrDTO getWithAttrGroupId(Long attrId);

    List<AttrDTO> attrListWithoutRelation(Long attrgroupId);

    List<Long> selectSearchAttrs(@Param("attrIds") List<Long> attrIds);
}