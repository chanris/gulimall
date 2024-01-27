package com.chanris.gulimall.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 会员
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
@ApiModel(value = "会员")
public class MemberDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "会员等级id")
	private Long levelId;

	@ApiModelProperty(value = "用户名")
	private String username;

	@ApiModelProperty(value = "密码")
	private String password;

	@ApiModelProperty(value = "昵称")
	private String nickname;

	@ApiModelProperty(value = "手机号码")
	private String mobile;

	@ApiModelProperty(value = "邮箱")
	private String email;

	@ApiModelProperty(value = "头像")
	private String header;

	@ApiModelProperty(value = "性别")
	private Integer gender;

	@ApiModelProperty(value = "生日")
	private Date birth;

	@ApiModelProperty(value = "所在城市")
	private String city;

	@ApiModelProperty(value = "职业")
	private String job;

	@ApiModelProperty(value = "个性签名")
	private String sign;

	@ApiModelProperty(value = "用户来源")
	private Integer sourceType;

	@ApiModelProperty(value = "积分")
	private Integer integration;

	@ApiModelProperty(value = "成长值")
	private Integer growth;

	@ApiModelProperty(value = "启用状态")
	private Integer status;

	@ApiModelProperty(value = "注册时间")
	private Date createTime;


}