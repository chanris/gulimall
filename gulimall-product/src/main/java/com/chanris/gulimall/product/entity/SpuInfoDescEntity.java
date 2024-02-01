package com.chanris.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * spu信息介绍
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
@TableName("pms_spu_info_desc")
public class SpuInfoDescEntity {

    /**
     * 商品id
     */
    @TableId
	private Long spuId;
    /**
     * 商品介绍
     */
	private String decript;
}