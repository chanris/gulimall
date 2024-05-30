package com.chanris.gulimall.order.config;

import com.chanris.gulimall.order.entity.OrderEntity;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author chenyue7@foxmail.com
 * @date 11/3/2024
 * @description 配置rabbitTemplate
 */
@Configuration
public class MyRabbitConfig {

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
}
