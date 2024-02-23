package com.chanris.gulimall.common.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;
import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 23/2/2024
 * @description
 */
@Data
public class SysUserTo {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @ApiModelProperty(value = "密码")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ApiModelProperty(value = "姓名", required = true)
    private String realName;

    @ApiModelProperty(value = "头像")
    private String headUrl;

    @ApiModelProperty(value = "性别   0：男   1：女    2：保密", required = true)
    private Integer gender;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "部门ID", required = true)
    private Long deptId;
    @ApiModelProperty(value = "状态  0：停用    1：正常", required = true)
    private Integer status;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "超级管理员   0：否   1：是")
    private Integer superAdmin;

    @ApiModelProperty(value = "角色ID列表")
    private List<Long> roleIdList;

    @ApiModelProperty(value = "部门名称")
    private String deptName;
}
