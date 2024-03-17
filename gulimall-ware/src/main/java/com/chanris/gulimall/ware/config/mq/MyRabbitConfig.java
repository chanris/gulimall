package com.chanris.gulimall.ware.config.mq;

import com.chanris.gulimall.ware.entity.WareSkuEntity;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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
 * @date 15/3/2024
 * @description
 */
@Configuration
public class MyRabbitConfig {

    @Resource
    RabbitTemplate rabbitTemplate;

    // 使用JSON发送和接收消息
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Exchange stockEventExchange() {
        return new TopicExchange("stock-event-exchange", true, false);
    }

    @Bean
    public Queue stockReleaseStockQueue() {
        return new Queue("stock.release.stock.queue", true, false, false);
    }

    @Bean
    public Queue stockDelayQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "stock-event-exchange");
        arguments.put("x-dead-letter-routing-key", "stock.release");
        arguments.put("x-message-ttl", 120000);
        return new Queue("stock.delay.queue",true, false, false, arguments);
    }

    @Bean
    public Binding stockReleaseStockQueueBinding() {
        return new Binding("stock.release.stock.queue", Binding.DestinationType.QUEUE,"stock-event-exchange","stock.release.#", null);
    }

    @Bean
    public Binding stockDelayQueueBinding() {
        return new Binding("stock.delay.queue", Binding.DestinationType.QUEUE,"stock-event-exchange","stock.locked", null);
    }

    /**
     * 得开一个监听 创建交换机、绑定、队列才能写到rabbit中
     * @param entity
     */
    @RabbitListener(queues = "stock.release.stock.queue")
    public void listener(WareSkuEntity entity) {

    }

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
}
