package com.chanris.gulimall.order;

import com.chanris.gulimall.order.vo.RabbitHelloMessageVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
class GulimallOrderApplicationTests {

    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private AmqpAdmin amqpAdmin;

    /**
     * 创建交换机
     */
    @Test
    void contextLoads() {
        System.out.println(amqpAdmin);
        DirectExchange directExchange = new DirectExchange("hello-java-exchange", true, false);
        amqpAdmin.declareExchange(directExchange);
        log.info("Exchange创建成功: {}", directExchange.getName());
    }

    @Test
    void createQueue() {
        Queue queue = new Queue("hello-java-queue", true, false, false);
        amqpAdmin.declareQueue(queue);
        log.info("Queue创建成功: {}", queue.getName());
    }

    @Test
    void createBinding() {
        // destination 目的地
        // destinationType 目的地类型
        // exchange 交换机
        // routingKey 路由键
        // arguments 自定义参数
        Binding binding = new Binding("hello-java-queue", Binding.DestinationType.QUEUE,"hello-java-exchange", "hello.java", null);
        amqpAdmin.declareBinding(binding);
        log.info("Binding 创建成功: {}", binding.toString());
    }

    @Test
    void sendMessage() {
        RabbitHelloMessageVo vo = new RabbitHelloMessageVo();
        vo.setMsg("Hello RabbitMQ");
        vo.setCode("0023");
        rabbitTemplate.convertAndSend("hello-java-exchange", "hello.java", vo);
        log.info("消息发送成功");
    }
    @Test
    void getMessage() {
        System.out.println(rabbitTemplate);
    }
}
