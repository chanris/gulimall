package com.chanris.gulimall.product.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 商品三级分类
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
@ApiModel(value = "商品三级分类")
public class CategoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "分类id")
	private Long catId;

	@ApiModelProperty(value = "分类名称")
	private String name;

	@ApiModelProperty(value = "父分类id")
	private Long parentCid;

	@ApiModelProperty(value = "层级")
	private Integer catLevel;

	@ApiModelProperty(value = "是否显示[0-不显示，1显示]")
	@TableLogic
	private Integer showStatus;

	@ApiModelProperty(value = "排序")
	private Integer sort;

	@ApiModelProperty(value = "图标地址")
	private String icon;

	@ApiModelProperty(value = "计量单位")
	private String productUnit;

	@ApiModelProperty(value = "商品数量")
	private Integer productCount;

	@TableField(exist = false)
	private List<CategoryDTO> children;
}