package com.chanris.gulimall.order.to;

import com.chanris.gulimall.order.entity.OrderEntity;
import com.chanris.gulimall.order.entity.OrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 12/3/2024
 * @description
 */
@Data
public class OrderCreateTo {

    // 订单实体类
    private OrderEntity order;
    // 每一个订单项
    private List<OrderItemEntity> orderItems;
    /** 订单计算的应付价格 **/
    private BigDecimal payPrice;
    /** 运费 **/
    private BigDecimal fare;
}
