package com.chanris.gulimall.order.config.mq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenyue7@foxmail.com
 * @date 11/3/2024
 * @description 配置rabbitTemplate
 */
@Configuration
public class OrderRabbitConfig {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 使用JSON系列化机制，进行消息转换
     *
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 设置 rabbit 消费者 接收到消息后ack和reject消息的回调函数
     */
    @PostConstruct
    public void initRabbitTemplate() {
        System.out.println("initRabbitTemplate...");
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             *
             * @param correlationData 当前消息的唯一id
             * @param ack 消息是否成功收到
             * @param cause 失败的原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("CorrelationData[" + correlationData + "]==>ack["+ ack + "]===>cause[" + cause + "]");
            }
        });

        /**
         * 设置消息抵达队列的确认回调
         */
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage message) {
                System.out.println(message.getMessage());
                System.out.println(message.getExchange());
                System.out.println(message.getRoutingKey());
                System.out.println(message.getReplyCode());
            }
        });
    }


    /**
     * 如果rabbit中没有该队列就会创建
     * 创建订单延迟队列 order.delay.queue
     * 所有已创建未支付的订单信息在这个队列里，若两分钟内没有被消费，
     * 则订单信息通过 路由键 order.release.order  进入 order.release.order.queue 队列，
     * 在order.release.order.queue 会反复消费订单信息，只到成功消费。
     * @return
     */
    @Bean
    public Queue orderDelayQueue() {
        // String name, boolean durable, boolean exclusive, boolean autoDelete, @Nullable Map<String, Object> arguments
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "order-event-exchange");
        arguments.put("x-dead-letter-routing-key", "order.release.order"); // 指定死信队列
        arguments.put("x-message-ttl", 60000);
        Queue queue = new Queue("order.delay.queue", true, false, false, arguments);
        return queue;
    }

    /**
     * 订单过期队列
     * @return
     */
    @Bean
    public Queue orderReleaseQueue() {
        Queue queue = new Queue("order.release.order.queue", true, false, false);
        return queue;
    }

    /**
     * 创建交换机
     *
     */
    @Bean
    public Exchange orderEventExchange() {
        return new TopicExchange("order-event-exchange", true, false);
    }

    /**
     * binding
     * routingKey: order.create.order
     * queue: order.delay.queue
     * exchange: order-event-exchange
     */
    @Bean
    public Binding orderCreateOrderBinding() {
        // String destination, DestinationType destinationType, String exchange, String routingKey, @Nullable Map<String, Object> arguments
        return new Binding("order.delay.queue", Binding.DestinationType.QUEUE, "order-event-exchange", "order.create.order", null);
    }

    /**
     * binding
     * routingKey: order.release.order
     * queue: order.release.order.queue
     * exchange: order-event-exchange
     */
    @Bean
    public Binding orderReleaseOrderBinding() {
        return new Binding("order.release.order.queue", Binding.DestinationType.QUEUE, "order-event-exchange", "order.release.order", null);
    }

    /**
     * 订单是否直接和库存是否进行绑定
     * @return
     */
    @Bean
    public Binding orderReleaseOtherBinding() {
        return new Binding("stock.release.stock.queue", Binding.DestinationType.QUEUE, "order-event-exchange", "order.release.other.#", null);
    }

    @Bean
    public Queue orderSeckillOrderQueue() {
        return new Queue("order.seckill.order.queue", true, false, false);
    }

    @Bean
    public Binding orderSeckillOrderBinding() {
        return new Binding("order.seckill.order.queue", Binding.DestinationType.QUEUE, "order-event-exchange", "order.seckill.order", null);
    }
}
