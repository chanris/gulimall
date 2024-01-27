package com.chanris.gulimall.ware.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 库存工作单
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
@ApiModel(value = "库存工作单")
public class WareOrderTaskDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "order_id")
	private Long orderId;

	@ApiModelProperty(value = "order_sn")
	private String orderSn;

	@ApiModelProperty(value = "收货人")
	private String consignee;

	@ApiModelProperty(value = "收货人电话")
	private String consigneeTel;

	@ApiModelProperty(value = "配送地址")
	private String deliveryAddress;

	@ApiModelProperty(value = "订单备注")
	private String orderComment;

	@ApiModelProperty(value = "付款方式【 1:在线付款 2:货到付款】")
	private Integer paymentWay;

	@ApiModelProperty(value = "任务状态")
	private Integer taskStatus;

	@ApiModelProperty(value = "订单描述")
	private String orderBody;

	@ApiModelProperty(value = "物流单号")
	private String trackingNo;

	@ApiModelProperty(value = "create_time")
	private Date createTime;

	@ApiModelProperty(value = "仓库id")
	private Long wareId;

	@ApiModelProperty(value = "工作单备注")
	private String taskComment;


}