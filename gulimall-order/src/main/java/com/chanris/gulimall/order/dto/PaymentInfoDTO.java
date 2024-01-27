package com.chanris.gulimall.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付信息表
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
@ApiModel(value = "支付信息表")
public class PaymentInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "订单号（对外业务号）")
	private String orderSn;

	@ApiModelProperty(value = "订单id")
	private Long orderId;

	@ApiModelProperty(value = "支付宝交易流水号")
	private String alipayTradeNo;

	@ApiModelProperty(value = "支付总金额")
	private BigDecimal totalAmount;

	@ApiModelProperty(value = "交易内容")
	private String subject;

	@ApiModelProperty(value = "支付状态")
	private String paymentStatus;

	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	@ApiModelProperty(value = "确认时间")
	private Date confirmTime;

	@ApiModelProperty(value = "回调内容")
	private String callbackContent;

	@ApiModelProperty(value = "回调时间")
	private Date callbackTime;


}