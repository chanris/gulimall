package com.chanris.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * spu信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
@TableName("pms_spu_info")
public class SpuInfoEntity {

    /**
     * 商品id
     */
	private Long id;
    /**
     * 商品名称
     */
	private String spuName;
    /**
     * 商品描述
     */
	private String spuDescription;
    /**
     * 所属分类id
     */
	private Long catalogId;
    /**
     * 品牌id
     */
	private Long brandId;
    /**
     * 
     */
	private BigDecimal weight;
    /**
     * 上架状态[0 - 下架，1 - 上架]
     */
	private Integer publishStatus;
    /**
     * 
     */
	private Date createTime;
    /**
     * 
     */
	private Date updateTime;
}