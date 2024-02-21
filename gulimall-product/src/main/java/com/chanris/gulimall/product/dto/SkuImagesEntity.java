package com.chanris.gulimall.product.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * sku图片
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
@ApiModel(value = "sku图片")
public class SkuImagesEntity implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "sku_id")
	private Long skuId;

	@ApiModelProperty(value = "图片地址")
	private String imgUrl;

	@ApiModelProperty(value = "排序")
	private Integer imgSort;

	@ApiModelProperty(value = "默认图[0 - 不是默认图，1 - 是默认图]")
	private Integer defaultImg;


}