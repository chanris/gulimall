package com.chanris.gulimall.order.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 退款信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
public class RefundInfoExcel {
    @ExcelProperty(value = "id")
    private Long id;
    @ExcelProperty(value = "退款的订单")
    private Long orderReturnId;
    @ExcelProperty(value = "退款金额")
    private BigDecimal refund;
    @ExcelProperty(value = "退款交易流水号")
    private String refundSn;
    @ExcelProperty(value = "退款状态")
    private Integer refundStatus;
    @ExcelProperty(value = "退款渠道[1-支付宝，2-微信，3-银联，4-汇款]")
    private Integer refundChannel;
    @ExcelProperty(value = "")
    private String refundContent;

}