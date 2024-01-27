package com.chanris.gulimall.ware.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * 
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
@ApiModel(value = "")
public class PurchaseDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "采购单id")
	private Long purchaseId;

	@ApiModelProperty(value = "采购商品id")
	private Long skuId;

	@ApiModelProperty(value = "采购数量")
	private Integer skuNum;

	@ApiModelProperty(value = "采购金额")
	private BigDecimal skuPrice;

	@ApiModelProperty(value = "仓库id")
	private Long wareId;

	@ApiModelProperty(value = "状态[0新建，1已分配，2正在采购，3已完成，4采购失败]")
	private Integer status;


}