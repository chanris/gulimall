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
public class WareOrderTaskDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "sku_id")
	private Long skuId;

	@ApiModelProperty(value = "sku_name")
	private String skuName;

	@ApiModelProperty(value = "购买个数")
	private Integer skuNum;

	@ApiModelProperty(value = "工作单id")
	private Long taskId;


}