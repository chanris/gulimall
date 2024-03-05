package com.chanris.gulimall.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {"com.chanris.gulimall.search.feign"})
@EnableDiscoveryClient // 开启服务注册与发现
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GulimallSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(GulimallSearchApplication.class, args);
	}

}
