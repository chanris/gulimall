package com.chanris.gulimall.order.service;

import com.chanris.gulimall.common.service.CrudService;
import com.chanris.gulimall.order.dto.OrderDTO;
import com.chanris.gulimall.order.entity.OrderEntity;
import com.chanris.gulimall.order.vo.OrderConfirmVo;

import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
public interface OrderService extends CrudService<OrderEntity, OrderDTO> {

    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException;
}