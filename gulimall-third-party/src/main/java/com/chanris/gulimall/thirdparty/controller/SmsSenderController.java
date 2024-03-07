package com.chanris.gulimall.thirdparty.controller;

import com.chanris.gulimall.common.utils.Result;
import com.chanris.gulimall.thirdparty.component.SmsComponent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author chenyue7@foxmail.com
 * @date 6/3/2024
 * @description
 */
@RestController
@RequestMapping("/sms")
public class SmsSenderController {
    @Resource
    private SmsComponent smsComponent;

    /**
     * 提供给别的服务进行调用
     * @param phone
     * @param code
     * @return
     */
    @GetMapping(value = "/sendCode")
    public Result sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code) {

        //发送验证码
        smsComponent.sendCode(phone,code);

        return new Result();
    }
}
