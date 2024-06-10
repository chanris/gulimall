package com.chanris.gulimall.order.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/10
 * @description
 */
@Slf4j
@RestController
public class OrderPayedListener {
    /**
     * 接收支付宝给我们的 支付成功的信息
     * @return
     */
    @PostMapping("/payed/notify")
    public String handleAlipayed(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        log.info("接收到支付宝 通知信息：{}", map);
        return "success";
    }
}
