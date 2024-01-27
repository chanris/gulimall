package com.chanris.gulimall.order.dao;

import com.chanris.gulimall.common.dao.BaseDao;
import com.chanris.gulimall.order.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Mapper
public interface OrderDao extends BaseDao<OrderEntity> {
	
}