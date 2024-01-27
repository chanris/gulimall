package com.chanris.gulimall.order.dao;

import com.chanris.gulimall.common.dao.BaseDao;
import com.chanris.gulimall.order.entity.OrderReturnReasonEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 退货原因
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Mapper
public interface OrderReturnReasonDao extends BaseDao<OrderReturnReasonEntity> {
	
}