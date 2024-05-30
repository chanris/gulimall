package com.chanris.gulimall.order.vo;

import lombok.Data;

/**
 * @author chenyue7@foxmail.com
 * @date 20/5/2024
 * @description 支付信息
 */
@Data
public class PayVo {
    private String out_trade_no; // 商单订单号 必填
    private String subject; // 订单名称
    private String total_amount; // 付款金额
    private String body; // 商品描述
}
