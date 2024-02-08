package com.chanris.gulimall.product.dto;

import com.chanris.gulimall.common.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
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

	@NotNull(groups = {UpdateGroup.class})
	@ApiModelProperty(value = "属性id")
	private Long attrId;

	@NotNull(groups = {UpdateGroup.class})
	@ApiModelProperty(value = "属性分组id")
	private Long attrGroupId;

	@ApiModelProperty(value = "属性组内排序")
	private Integer attrSort;


}