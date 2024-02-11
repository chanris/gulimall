package com.chanris.gulimall.member.dto;

import com.chanris.gulimall.common.validator.group.AddGroup;
import com.chanris.gulimall.common.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
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

	@NotNull(groups = {UpdateGroup.class})
	@ApiModelProperty(value = "id")
	private Long id;

	@NotNull(groups = {AddGroup.class})
	@Min(value = 1, groups = {AddGroup.class, UpdateGroup.class})
	@ApiModelProperty(value = "会员等级id")
	private Long levelId;

	@NotBlank(groups = {AddGroup.class})
	@ApiModelProperty(value = "用户名")
	private String username;

	@NotBlank(groups = {AddGroup.class})
	@Length(min = 6, max = 20, groups = {AddGroup.class, UpdateGroup.class})
	@ApiModelProperty(value = "密码")
	private String password;

	@NotBlank(groups = {AddGroup.class})
	@Length(min = 2, max =  20, groups = {UpdateGroup.class, AddGroup.class})
	@ApiModelProperty(value = "昵称")
	private String nickname;

	@NotBlank(groups = {AddGroup.class})
	@Pattern(regexp = "^1[3-9]\\d{9}$", groups = {UpdateGroup.class, AddGroup.class}, message = "手机号码格式错误")
	@ApiModelProperty(value = "手机号码")
	private String mobile;

	@ApiModelProperty(value = "邮箱")
	@Email(groups = {AddGroup.class, UpdateGroup.class})
	private String email;

	@ApiModelProperty(value = "头像")
	private String header;

	@ApiModelProperty(value = "性别")
	@NotNull(groups = {AddGroup.class})
	@Range(min = 0, max = 1, groups = {AddGroup.class, UpdateGroup.class})
	private Integer gender;

	@Past(groups = {AddGroup.class, UpdateGroup.class})
	@ApiModelProperty(value = "生日")
	private Date birth;

	@NotBlank(groups = {AddGroup.class})
	@Length(min = 2, max = 20, groups = {UpdateGroup.class, AddGroup.class})
	@ApiModelProperty(value = "所在城市")
	private String city;

	@ApiModelProperty(value = "职业")
	private String job;

	@ApiModelProperty(value = "个性签名")
	private String sign;

	@NotNull(groups = {AddGroup.class})
	@Range(min= 0, max = 10000, groups = {UpdateGroup.class, AddGroup.class})
	@ApiModelProperty(value = "用户来源")
	private Integer sourceType;

	@NotNull(groups = {AddGroup.class})
	@Range(groups = {AddGroup.class, UpdateGroup.class}) // 默认 min = 0
	@ApiModelProperty(value = "积分")
	private Integer integration;

	@NotNull(groups = {AddGroup.class})
	@Range(groups = {AddGroup.class, UpdateGroup.class})
	@ApiModelProperty(value = "成长值")
	private Integer growth;

	@NotNull(groups = {AddGroup.class})
	@Range(min = 0, max = 1, groups = {AddGroup.class, UpdateGroup.class})
	@ApiModelProperty(value = "启用状态")
	private Integer status;

	@NotNull(groups = {AddGroup.class})
	@PastOrPresent(groups = {AddGroup.class, UpdateGroup.class})
	@ApiModelProperty(value = "注册时间")
	private Date createTime;

}