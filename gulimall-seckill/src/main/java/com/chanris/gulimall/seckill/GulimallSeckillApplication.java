package com.chanris.gulimall.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/11
 * @description
 * 1、整合Sentinel
 *  1）导入依赖 spring-cloud-starter-alibaba-sentinel
 *  2）下载sentinel-dashboard控制台
 *  3）配置sentinel-dashboard控制台地址信息
 *  4）在控制台中调整和查询接口流量访问信息、并配置限量、熔断、服务降级规则
 *
 * 2、每一个微服务都导入actuator：并配合 management.endpoints.web.exposure.include='*'
 * 3、自定义sentinel 流控 返回的数据
 * 4、使用Sentinel来抱回feign远程调用、熔断
 *  1）调用方的熔断保护：feign.sentinel.enable=true
 *  2）调用方手动指定远程服务的降级策略。远程服务被降级处理。触发我们的熔断回调方法
 *  3）超大流量的时候，必须牺牲一些远程服务。在服务的提供方（远程服务）指定降级策略；
 *      提供方是在运行，但是不允许自己的业务逻辑，返回的是默认的降级数据（限流的数据）
 * 5、自定义受保护的资源
 *  1）代码
 *      try(Entry entry = SphU.entry("seckillSkus")) {
 *          //业务逻辑
 *      }catch(Exception e) {}
 *  2）基于注解
 *
 */
@EnableRedisHttpSession  // redis存储 spring session
@EnableFeignClients(basePackages = {"com.chanris.gulimall.seckill.feign"})
@EnableDiscoveryClient // 开启服务注册与发现
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GulimallSeckillApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallSeckillApplication.class, args);
    }

}