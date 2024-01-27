package com.chanris.gulimall.order.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付信息表
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
public class PaymentInfoExcel {
    @ExcelProperty(value = "id")
    private Long id;
    @ExcelProperty(value = "订单号（对外业务号）")
    private String orderSn;
    @ExcelProperty(value = "订单id")
    private Long orderId;
    @ExcelProperty(value = "支付宝交易流水号")
    private String alipayTradeNo;
    @ExcelProperty(value = "支付总金额")
    private BigDecimal totalAmount;
    @ExcelProperty(value = "交易内容")
    private String subject;
    @ExcelProperty(value = "支付状态")
    private String paymentStatus;
    @ExcelProperty(value = "创建时间")
    private Date createTime;
    @ExcelProperty(value = "确认时间")
    private Date confirmTime;
    @ExcelProperty(value = "回调内容")
    private String callbackContent;
    @ExcelProperty(value = "回调时间")
    private Date callbackTime;

}