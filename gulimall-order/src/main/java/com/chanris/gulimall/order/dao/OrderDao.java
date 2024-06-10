package com.chanris.gulimall.order.dao;

import com.chanris.gulimall.common.dao.BaseDao;
import com.chanris.gulimall.order.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Mapper
public interface OrderDao extends BaseDao<OrderEntity> {

    /**
     * 根据订单号更新状态
     * @param outTradeNo
     * @param code
     */
    void updateOrderStatus(@Param("orderSn") String outTradeNo,@Param("status") Integer code);
}