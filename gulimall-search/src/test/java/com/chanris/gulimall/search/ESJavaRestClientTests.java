package com.chanris.gulimall.search;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import lombok.ToString;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 27/2/2024
 * @description
 */
@SpringBootTest
public class ESJavaRestClientTests {

    @Resource
    private RestHighLevelClient client;

    @ToString
    class Account {
        private int account_number;
        private int balance;
        private String firstname;
        private String lastname;
        private int age;
        private String gender;
        private String address;
        private String employer;
        private String email;
        private String city;
        private String state;
        public void setAccount_number(int account_number) {
            this.account_number = account_number;
        }
        public int getAccount_number() {
            return account_number;
        }

        public void setBalance(int balance) {
            this.balance = balance;
        }
        public int getBalance() {
            return balance;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }
        public String getFirstname() {
            return firstname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }
        public String getLastname() {
            return lastname;
        }

        public void setAge(int age) {
            this.age = age;
        }
        public int getAge() {
            return age;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
        public String getGender() {
            return gender;
        }

        public void setAddress(String address) {
            this.address = address;
        }
        public String getAddress() {
            return address;
        }

        public void setEmployer(String employer) {
            this.employer = employer;
        }
        public String getEmployer() {
            return employer;
        }

        public void setEmail(String email) {
            this.email = email;
        }
        public String getEmail() {
            return email;
        }

        public void setCity(String city) {
            this.city = city;
        }
        public String getCity() {
            return city;
        }

        public void setState(String state) {
            this.state = state;
        }
        public String getState() {
            return state;
        }

    }

    /**
     * 测试检索
     */
    @Test
    void test02() throws IOException {
        // 1. 创建检索请求
        SearchRequest request = new SearchRequest("bank");
        // 2.指定检索 索引
        // request.indices("bank");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT); // 同步执行
        System.out.println(response.toString());
    }

    /**
     * 测试 聚合检索
     */
    @Test
    void test04() throws IOException {
        // 1. 创建检索请求
        SearchRequest request = new SearchRequest("bank");
        // 2.指定检索 索引
        // request.indices("bank");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        // 聚合条件
        // 1. 安装年龄的值分布进行聚合
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("ageAgg").field("age").size(100);
        // 2. 计算平均薪资
        AvgAggregationBuilder avgAggregationBuilder = AggregationBuilders.avg("balanceAvg").field("balance");

        sourceBuilder.aggregation(termsAggregationBuilder).aggregation(avgAggregationBuilder);
        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT); // 同步执行
//        System.out.println(response.toString());

        // 3.1 获得命中的文档信息
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits) {
            //hit.getIndex(); // 获得 索引
           // hit.getId(); // 获得id
            String sourceAsString = hit.getSourceAsString();
            Account account = JSONObject.parseObject(sourceAsString, Account.class);
            System.out.println(account);
        }

        // 3.2 获得这次检索到的分析信息
        Aggregations aggregations = response.getAggregations();
        for (Aggregation agg :aggregations.asList()) {
                break; // 增强foreach 可以 break
//            agg.getName();
//            agg.getType();
        }
        Terms ageAgg = aggregations.get("ageAgg");
        for (Terms.Bucket bucket : ageAgg.getBuckets()) {
            System.out.println("年龄:" + bucket.getKeyAsString());
        }

        Avg balanceAvg = aggregations.get("balanceAvg");
        System.out.println("平均薪资:" + balanceAvg.getValue());
    }

    /**
     * 测试检索 分析结果
     * @throws IOException
     */
    @Test
    void test03() throws IOException {
        // 1. 创建检索请求
        SearchRequest request = new SearchRequest("bank");
        // 2.指定检索 索引
        // request.indices("bank");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));
        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT); // 同步执行
        System.out.println(response.toString());
    }

    /**
     * 测试存储数据到es
     */
    @Test
    void test01() throws IOException {
        IndexRequest indexRequest = new IndexRequest("user");
        indexRequest.id("1"); // 设置数据id
        User user = new User();
        user.setAge(18);
        user.setUsername("chanris");
        user.setGender("boy");
        String json = JSON.toJSONString(user);
        indexRequest.source(json, XContentType.JSON); // 设置source 一定要设置类型
        IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT); // 同步发送请求
        System.out.println(response.toString());
    }

    @Data
    class User {
        private String username;
        private String gender;
        private Integer age;
    }
}
