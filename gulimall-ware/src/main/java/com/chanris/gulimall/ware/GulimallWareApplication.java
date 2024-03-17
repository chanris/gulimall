package com.chanris.gulimall.ware;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
@EnableRabbit // rabbit 消息队列
@EnableFeignClients(basePackages = {"com.chanris.gulimall.ware.feign"})   // 开启远程调用功
@EnableDiscoveryClient
@SpringBootApplication
public class GulimallWareApplication {

	public static void main(String[] args) {
		SpringApplication.run(GulimallWareApplication.class, args);
	}

}
