package com.chanris.gulimall.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 会员收货地址
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
@ApiModel(value = "会员收货地址")
public class MemberReceiveAddressDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "member_id")
	private Long memberId;

	@ApiModelProperty(value = "收货人姓名")
	private String name;

	@ApiModelProperty(value = "电话")
	private String phone;

	@ApiModelProperty(value = "邮政编码")
	private String postCode;

	@ApiModelProperty(value = "省份/直辖市")
	private String province;

	@ApiModelProperty(value = "城市")
	private String city;

	@ApiModelProperty(value = "区")
	private String region;

	@ApiModelProperty(value = "详细地址(街道)")
	private String detailAddress;

	@ApiModelProperty(value = "省市区代码")
	private String areacode;

	@ApiModelProperty(value = "是否默认")
	private Integer defaultStatus;


}