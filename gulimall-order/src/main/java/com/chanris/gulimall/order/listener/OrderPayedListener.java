package com.chanris.gulimall.order.listener;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.chanris.gulimall.order.config.AlipayTemplate;
import com.chanris.gulimall.order.service.OrderService;
import com.chanris.gulimall.order.vo.PayAsyncVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/10
 * @description
 * 收单： 订单在支付页，不支付，一直刷新，订单过期了才支付，订单状态改为已支付了，但是库存解锁了
 *    1. 使用支付宝自动收单功能。只要一段时间不支付，就不能支付了
 *    2. 由于时延等问题。订单解锁完成，正在解锁库存时候，异步通知才到
 *       订单解锁，手动调用收单
 *    3. 网络阻塞问题，订单支付成功的异步通知一直不到达
 *      查询订单列表时，ajax获取当前未支付的订单状态，查询订单状态时，再获取一下支付包此订单的状态
 *
 */
@Slf4j
@RestController
public class OrderPayedListener {

    @Resource
    private OrderService orderService;

    @Resource
    private AlipayTemplate alipayTemplate;

    /**
     * 接收支付宝给我们的 支付成功的信息
     * @return
     */
    @PostMapping("/payed/notify")
    public String handleAlipayed(PayAsyncVo vo, HttpServletRequest request) throws AlipayApiException {
        // 验签：保证是支付宝发给我们的数据
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for(Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        boolean  signVerified = AlipaySignature.rsaCheckV1(params, alipayTemplate.alipay_public_key, alipayTemplate.getCharset(),alipayTemplate.getSign_type());
        String result = "error";
        if (signVerified) {
            result = orderService.handlePayResult(vo);
        }
        log.info("接收到支付宝 通知信息：{}", params);
        return result;
    }
}
