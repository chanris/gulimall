package com.chanris.gulimall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenyue7@foxmail.com
 * @date 12/3/2024
 * @description 封装订单提交数据
 */
@Data
public class OrderSubmitVo {
    private Long addrId;
    private Integer payType;
    // 防重令牌
    private String orderToken;
    private String note;
    private BigDecimal payPrice; // 验价
}
