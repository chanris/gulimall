package com.chanris.gulimall.order.web;

import com.alipay.api.AlipayApiException;
import com.chanris.gulimall.order.config.AlipayTemplate;
import com.chanris.gulimall.order.service.OrderService;
import com.chanris.gulimall.order.vo.PayVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author chenyue7@foxmail.com
 * @date 20/5/2024
 * @description
 */
@Slf4j
@Controller
public class PayWebController {

    @Resource
    private AlipayTemplate alipayTemplate;

    @Resource
    private OrderService orderService;

    @ResponseBody
    @GetMapping(value = "/aliPayOrder", produces = {"text/html"})
    public String payOrder(@RequestParam("orderSn") String orderSn) throws AlipayApiException {
        PayVo payVo = orderService.getOrderPay(orderSn);
        String pay = alipayTemplate.pay(payVo);
        log.info("pay: {}", pay);
        return pay;
    }
}
