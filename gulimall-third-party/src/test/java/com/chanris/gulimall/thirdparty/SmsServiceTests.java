package com.chanris.gulimall.thirdparty;

import com.chanris.gulimall.thirdparty.component.SmsComponent;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author chenyue7@foxmail.com
 * @date 6/3/2024
 * @description
 */
@SpringBootTest
public class SmsServiceTests {

    @Resource
    SmsComponent smsComponent;

    @Test
    void test01() {
        smsComponent.sendCode("18081484104", "5432");
    }
}
