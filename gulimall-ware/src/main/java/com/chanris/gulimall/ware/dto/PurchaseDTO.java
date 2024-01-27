package com.chanris.gulimall.ware.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * 采购信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
@ApiModel(value = "采购信息")
public class PurchaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "采购单id")
	private Long id;

	@ApiModelProperty(value = "采购人id")
	private Long assigneeId;

	@ApiModelProperty(value = "采购人名")
	private String assigneeName;

	@ApiModelProperty(value = "联系方式")
	private String phone;

	@ApiModelProperty(value = "优先级")
	private Integer priority;

	@ApiModelProperty(value = "状态")
	private Integer status;

	@ApiModelProperty(value = "仓库id")
	private Long wareId;

	@ApiModelProperty(value = "总金额")
	private BigDecimal amount;

	@ApiModelProperty(value = "创建日期")
	private Date createTime;

	@ApiModelProperty(value = "更新日期")
	private Date updateTime;


}