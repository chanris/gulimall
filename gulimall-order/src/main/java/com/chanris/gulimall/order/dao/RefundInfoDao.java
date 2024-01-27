package com.chanris.gulimall.order.dao;

import com.chanris.gulimall.common.dao.BaseDao;
import com.chanris.gulimall.order.entity.RefundInfoEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 退款信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Mapper
public interface RefundInfoDao extends BaseDao<RefundInfoEntity> {
	
}