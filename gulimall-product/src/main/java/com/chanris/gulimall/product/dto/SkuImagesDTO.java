package com.chanris.gulimall.product.dto;

import lombok.Data;

/**
 * @author chenyue7@foxmail.com
 * @date 6/3/2024
 * @description
 */
@Data
public class SkuImagesDTO {

    private Long id;
    /**
     * spu_id
     */
    private Long spuId;
    /**
     * 图片名
     */
    private String imgName;
    /**
     * 图片地址
     */
    private String imgUrl;
    /**
     * 顺序
     */
    private Integer imgSort;
    /**
     * 是否默认图
     */
    private Integer defaultImg;
}
