package com.chanris.gulimall.order;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 使用RabbitMQ
 * 1. 引入amqp场景：RabbitAutoConfiguration自动生效
 * 2. 给容器配置了 RabbitTemplate、AmqpAdmin、 CachingConnectionFactory、RabbitMessageTemplates
 * 所有的属性都是 spring.rabbitmq
 * @ConfigurationProperties(pref="spring.rabbitmq")
 * 3. @EnableRabbit: 启动rabbit
 *
 * 4. 接收消息 @RabbitListener：可以标在类(监听不同的队列)、方法上 @RabbitHandler 只能标在方法上，可以重载不同的消息
 * 5. rabbitmq消息确认机制-可靠抵达
 *    为了保证消息不丢失-可靠抵达 可以使用事务消息，性能下降250倍，为此引入确认机制
 *    publisher confirmCallback确认模式
 *    publisher returnCallback 未投递到 queue 退回模式
 *    consumer ack机制
 *
 * 3.rabbitmq消息确认机制--客户端
 *  客户端收到消息后，确定是否签收消息，可以拒绝消息 ack=true，拒绝消息可以重新入队或直接丢去
 *
 *  ============================ 订单 =========================
 *
 * Seata控制分布式事务
 * AT: 自动事务回滚，不支持高并发场景
 * TCC: 自定义事务准备、提交、回滚逻辑
 *
 * 柔性事务： 可靠消息+保证最终一致性方案（异步确保法）
 * 业务处理服务在业务事务提交之前，向实时消息服务请求发送消息，实时消息只记录消息数据，而不是真正的发送。
 * 业务处理服务在业务事务提交之后，向实时消息服务确认发送。只要得到确认发送指令后，实时消息服务才会真正发送。
 */
@EnableFeignClients(basePackages = "com.chanris.gulimall.order.feign")
@EnableRedisHttpSession // Spring Session
@EnableRabbit // rabbit 消息队列
@EnableDiscoveryClient // nacos
@SpringBootApplication // spring boot webmvc
public class GulimallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallOrderApplication.class, args);
    }

}
