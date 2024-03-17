package com.chanris.gulimall.order.config.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenyue7@foxmail.com
 * @date 15/3/2024
 * @description
 *
 * exchange: 交换机的名称
 * routing-key：匹配对各队列的正则规则
 * queue name:
 * binding: 绑定交换机 和 队列
 */
@Configuration
public class RabbitInitConfig {

    /**
     * 如果rabbit中没有该队列就会创建
     * @return
     */
    @Bean
    public Queue orderDelayQueue() {
        // String name, boolean durable, boolean exclusive, boolean autoDelete, @Nullable Map<String, Object> arguments
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "order-event-exchange");
        arguments.put("x-dead-letter-routing-key", "order.release.order");
        arguments.put("x-message-ttl", 60000);
        Queue queue = new Queue("order.delay.queue", true, false, false, arguments);
        return queue;
    }

    @Bean
    public Queue orderReleaseQueue() {
        Queue queue = new Queue("order.release.order.queue", true, false, false);
        return queue;
    }

    @Bean
    public Exchange orderEventExchange() {
        return new TopicExchange("order-event-exchange", true, false);
    }

    @Bean
    public Binding orderCreateOrderBinding() {
        // String destination, DestinationType destinationType, String exchange, String routingKey, @Nullable Map<String, Object> arguments
        return new Binding("order.delay.queue", Binding.DestinationType.QUEUE, "order-event-exchange", "order.create.order", null);
    }

    @Bean
    public Binding orderReleaseOrderBinding() {
        return new Binding("order.release.order.queue", Binding.DestinationType.QUEUE, "order-event-exchange", "order.release.order", null);
    }
}
