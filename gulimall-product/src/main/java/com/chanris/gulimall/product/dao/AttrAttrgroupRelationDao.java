package com.chanris.gulimall.product.dao;

import com.chanris.gulimall.common.dao.BaseDao;
import com.chanris.gulimall.product.dto.AttrDTO;
import com.chanris.gulimall.product.entity.AttrAttrgroupRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseDao<AttrAttrgroupRelationEntity> {

    void deleteByAttrIdAndAttrGroupId(@Param("attrId") Long attrId, @Param("attrGroupId") Long attrGroupId);
    void deleteByAttrGroupIds(@Param("list") List<Long> attrGroupIds);
    void deleteByAttrIds(@Param("list") List<Long> attrIds);
    void updateByAttrId(AttrDTO attrDTO);

}