package com.chanris.gulimall.order.web;

import com.chanris.gulimall.order.service.OrderService;
import com.chanris.gulimall.order.vo.OrderConfirmVo;
import com.chanris.gulimall.order.vo.OrderSubmitVo;
import com.chanris.gulimall.order.vo.SubmitOrderRespVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * @author chenyue7@foxmail.com
 * @date 11/3/2024
 * @description
 */
@Controller
public class OrderWebController {

    @Resource
    private OrderService orderService;

    @GetMapping("/toTrade")
    public String toTrade(Model model) throws ExecutionException, InterruptedException {
        OrderConfirmVo confirmVo = orderService.confirmOrder();
        model.addAttribute("confirmOrderData", confirmVo);
        return "confirm";
    }

    /**
     * 下单
     * 重定向会丢失数据
     * @param orderSubmitVo
     * @return
     */
    @PostMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo vo, Model model, RedirectAttributes attributes) {
        try {
            SubmitOrderRespVo responseVo = orderService.submitOrder(vo);
            //下单成功来到支付选择页
            //下单失败回到订单确认页重新确定订单信息
            if (responseVo.getCode() == 0) {
                //成功
                model.addAttribute("submitOrderResp",responseVo);
                return "pay";
            } else {
                String msg = "下单失败";
                switch (responseVo.getCode()) {
                    case 1: msg += "令牌订单信息过期，请刷新再次提交"; break;
                    case 2: msg += "订单商品价格发生变化，请确认后再次提交"; break;
                    case 3: msg += "库存锁定失败，商品库存不足"; break;
                }
                attributes.addFlashAttribute("msg",msg);
                return "redirect:http://order.gulimall.com/toTrade";
            }
        } catch (Exception e) {
                String message = e.getMessage();
                attributes.addFlashAttribute("msg",message);
            return "redirect:http://order.gulimall.com/toTrade";
        }
    }
}
