package com.chanris.gulimall.ware.dao;

import com.chanris.gulimall.common.dao.BaseDao;
import com.chanris.gulimall.ware.entity.WareSkuEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Mapper
public interface WareSkuDao extends BaseDao<WareSkuEntity> {
	
}