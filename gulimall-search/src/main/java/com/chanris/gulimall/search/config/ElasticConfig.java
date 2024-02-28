package com.chanris.gulimall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenyue7@foxmail.com
 * @date 27/2/2024
 * @description es配置， 配置 RHLC 客户端
 */
@Configuration
public class ElasticConfig {

    public static final RequestOptions COMMON_OPTIONS = RequestOptions.DEFAULT;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        RestClientBuilder clientBuilder = null;
        clientBuilder = RestClient.builder(new HttpHost("192.168.125.129", 9200, "http"));
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(clientBuilder);
        return restHighLevelClient;
    }

}
