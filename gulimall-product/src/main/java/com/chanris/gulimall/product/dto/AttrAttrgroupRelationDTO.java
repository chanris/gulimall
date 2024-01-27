package com.chanris.gulimall.product.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 属性&属性分组关联
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
@ApiModel(value = "属性&属性分组关联")
public class AttrAttrgroupRelationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "属性id")
	private Long attrId;

	@ApiModelProperty(value = "属性分组id")
	private Long attrGroupId;

	@ApiModelProperty(value = "属性组内排序")
	private Integer attrSort;


}