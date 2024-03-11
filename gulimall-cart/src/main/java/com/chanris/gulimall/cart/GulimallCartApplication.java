package com.chanris.gulimall.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@EnableFeignClients(basePackages = "com.chanris.gulimall.cart.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class GulimallCartApplication {

	public static void main(String[] args) {
		SpringApplication.run(GulimallCartApplication.class, args);
	}

}
