package com.chanris.gulimall.search;


import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
@Slf4j
@SpringBootTest
class GulimallSearchApplicationTests {

	@Resource
	private RestHighLevelClient client;

	@Test
	void contextLoads() {
		System.out.println(client);
	}

	@Test
	void test02() {
		try {
			System.out.println(1 / 0);
		}catch (Exception e) {
			log.error("发生错误: ", e);
		}
	}
}
