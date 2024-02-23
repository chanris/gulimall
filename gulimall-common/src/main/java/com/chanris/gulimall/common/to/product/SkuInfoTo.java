package com.chanris.gulimall.common.to.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenyue7@foxmail.com
 * @date 23/2/2024
 * @description
 */
@Data
public class SkuInfoTo {

    @ApiModelProperty(value = "skuId")
    private Long skuId;

    @ApiModelProperty(value = "spuId")
    private Long spuId;

    @ApiModelProperty(value = "sku名称")
    private String skuName;

    @ApiModelProperty(value = "sku介绍描述")
    private String skuDesc;

    @ApiModelProperty(value = "所属分类id")
    private Long catalogId;

    @ApiModelProperty(value = "品牌id")
    private Long brandId;

    @ApiModelProperty(value = "默认图片")
    private String skuDefaultImg;

    @ApiModelProperty(value = "标题")
    private String skuTitle;

    @ApiModelProperty(value = "副标题")
    private String skuSubtitle;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "销量")
    private Long saleCount;
}
