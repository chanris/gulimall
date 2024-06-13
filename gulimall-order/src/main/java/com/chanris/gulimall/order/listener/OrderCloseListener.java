package com.chanris.gulimall.order.listener;

import com.chanris.gulimall.order.entity.OrderEntity;
import com.chanris.gulimall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author chenyue7@foxmail.com
 * @date 30/5/2024
 * @description
 */
@Slf4j
@Service
@RabbitListener(queues = "order.release.order.queue")
public class OrderCloseListener {

    @Resource
    private OrderService orderService;

    @RabbitHandler
    public void listener(OrderEntity entity, Channel channel, Message message) throws IOException {
        log.info("收到过期的订单信息：关闭订单: " + entity.getOrderSn());
        try {
            orderService.closeOrder(entity);
            //手动调用支付宝收单
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("关闭订单成功：{}", entity.getOrderSn());
        }catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
            log.info("关闭订单失败：{}", entity.getOrderSn());
        }
    }
}
