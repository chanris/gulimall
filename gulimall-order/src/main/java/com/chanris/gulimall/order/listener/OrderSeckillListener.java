package com.chanris.gulimall.order.listener;

import com.chanris.gulimall.common.to.mq.SeckillOrderTo;
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
 * @date 2024/6/13
 * @description 监听秒杀订单
 */
@Slf4j
@Service
@RabbitListener(queues = "order.seckill.order.queue")
public class OrderSeckillListener {
    @Resource
    private OrderService orderService;

    @RabbitHandler
    public void listener(SeckillOrderTo orderTo, Channel channel, Message message) throws IOException {
        log.info("收到秒杀单: {}", orderTo);
        try {
            orderService.createSeckillOrder(orderTo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}
