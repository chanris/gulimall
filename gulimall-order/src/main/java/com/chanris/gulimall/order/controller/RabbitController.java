package com.chanris.gulimall.order.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author chenyue7@foxmail.com
 * @date 11/3/2024
 * @description @RabbitListener：可以标在类(监听不同的队列)、方法上 @RabbitHandler 只能标在方法上，可以重载不同的消息
 */
@RestController
public class RabbitController {

    @Resource
    private RabbitTemplate rabbitTemplate;


}
