package com.chanris.gulimall.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * 会员等级
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
@ApiModel(value = "会员等级")
public class MemberLevelDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "等级名称")
	private String name;

	@ApiModelProperty(value = "等级需要的成长值")
	private Integer growthPoint;

	@ApiModelProperty(value = "是否为默认等级[0->不是；1->是]")
	private Integer defaultStatus;

	@ApiModelProperty(value = "免运费标准")
	private BigDecimal freeFreightPoint;

	@ApiModelProperty(value = "每次评价获取的成长值")
	private Integer commentGrowthPoint;

	@ApiModelProperty(value = "是否有免邮特权")
	private Integer priviledgeFreeFreight;

	@ApiModelProperty(value = "是否有会员价格特权")
	private Integer priviledgeMemberPrice;

	@ApiModelProperty(value = "是否有生日特权")
	private Integer priviledgeBirthday;

	@ApiModelProperty(value = "备注")
	private String note;


}