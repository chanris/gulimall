package com.chanris.gulimall.order.service;

import com.chanris.gulimall.common.page.PageData;
import com.chanris.gulimall.common.service.CrudService;
import com.chanris.gulimall.common.to.OrderTo;
import com.chanris.gulimall.common.to.mq.SeckillOrderTo;
import com.chanris.gulimall.order.dto.OrderDTO;
import com.chanris.gulimall.order.entity.OrderEntity;
import com.chanris.gulimall.order.vo.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
public interface OrderService extends CrudService<OrderEntity, OrderDTO> {

    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException;

    SubmitOrderRespVo submitOrder(OrderSubmitVo orderSubmitVo);

    OrderEntity getOrderByOrderSn(String orderSn);

    void closeOrder(OrderEntity order);

    PayVo getOrderPay(String orderSn);

    PageData<OrderDTO> listWithItem(Map<String, Object> params);

    String handlePayResult(PayAsyncVo vo);

    void createSeckillOrder(SeckillOrderTo orderTo);
}