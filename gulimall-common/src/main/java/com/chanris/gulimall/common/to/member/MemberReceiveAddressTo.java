package com.chanris.gulimall.common.to.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenyue7@foxmail.com
 * @date 12/3/2024
 * @description
 */
@Data
public class MemberReceiveAddressTo {
    private Long id;

    private Long memberId;

    private String name;

    private String phone;

    private String postCode;

    private String province;

    private String city;

    private String region;

    private String detailAddress;

    private String areacode;

    private Integer defaultStatus;
}
