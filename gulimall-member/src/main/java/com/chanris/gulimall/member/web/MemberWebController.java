package com.chanris.gulimall.member.web;

import com.alibaba.fastjson2.JSON;
import com.chanris.gulimall.common.page.PageData;
import com.chanris.gulimall.common.to.OrderTo;
import com.chanris.gulimall.common.utils.Result;
import com.chanris.gulimall.member.feign.OrderFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenyue7@foxmail.com
 * @date 31/5/2024
 * @description
 */
@Slf4j
@Controller
public class MemberWebController {

    @Resource
    private OrderFeignService orderFeignService;

    @GetMapping("/memberOrder.html")
    public String memberOrderPage(@RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
                                  Model model, HttpServletRequest request) {
        Map<String, Object> page = new HashMap<>();
        page.put("page", pageNum.toString());
        Result<PageData<OrderTo>> r = orderFeignService.listWithItem(page);
        log.info(JSON.toJSONString(r));
        // 查出当前登录的用户的所有订单列表数据
        model.addAttribute("orders", r);
        return "orderList";
    }
}
