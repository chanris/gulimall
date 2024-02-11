package com.chanris.gulimall.product.dto;

import com.chanris.gulimall.common.validator.group.AddGroup;
import com.chanris.gulimall.common.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


/**
 * 属性分组
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
@ApiModel(value = "属性分组")
public class AttrGroupDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@NotNull(groups = {UpdateGroup.class})
	@ApiModelProperty(value = "分组id")
	private Long attrGroupId;

	@NotBlank(groups = {AddGroup.class})
	@ApiModelProperty(value = "组名")
	private String attrGroupName;

	@ApiModelProperty(value = "排序")
	private Integer sort;

	@ApiModelProperty(value = "描述")
	private String descript;

	@ApiModelProperty(value = "组图标")
	private String icon;

	@ApiModelProperty(value = "所属分类id")
	private Long catelogId;

	// non db field

	private List<AttrDTO> attrs;
}