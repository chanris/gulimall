package com.chanris.gulimall.common.to.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenyue7@foxmail.com
 * @date 5/3/2024
 * @description
 */
@Data
public class AttrTo {

    private Long attrId;

    private String attrName;

    private Integer searchType;

    private Integer valueType;

    private String icon;

    private String valueSelect;

    private Integer attrType;

    private Long enable;

    private Long catelogId;

    private Integer showDesc;

    // non db field
    private Long attrGroupId;
    private String groupName;
    private String catelogName;
}
