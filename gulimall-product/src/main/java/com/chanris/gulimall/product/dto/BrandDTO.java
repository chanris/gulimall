package com.chanris.gulimall.product.dto;

import com.chanris.gulimall.common.validator.group.AddGroup;
import com.chanris.gulimall.common.validator.group.DefaultGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;


/**
 * 品牌
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
@ApiModel(value = "品牌")
public class BrandDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "品牌id")
	private Long brandId;

	@NotBlank(groups = {AddGroup.class})
	@Size(min = 2, max = 20, groups = {AddGroup.class})
	@ApiModelProperty(value = "品牌名")
	private String name;

	@ApiModelProperty(value = "品牌logo地址")
	private String logo;

	@ApiModelProperty(value = "介绍")
	private String descript;

	@ApiModelProperty(value = "显示状态[0-不显示；1-显示]")
	private Integer showStatus;

	@NotBlank(groups = {AddGroup.class})
	@Length(min = 1, max = 1, groups = {AddGroup.class})
	@ApiModelProperty(value = "检索首字母")
	private String firstLetter;

	@ApiModelProperty(value = "排序")
	private Integer sort;


}