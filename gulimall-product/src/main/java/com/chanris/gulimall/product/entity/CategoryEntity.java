package com.chanris.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 商品三级分类
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
@TableName("pms_category")
public class CategoryEntity {

    /**
     * 分类id
     */
	private Long catId;
    /**
     * 分类名称
     */
	private String name;
    /**
     * 父分类id
     */
	private Long parentCid;
    /**
     * 层级
     */
	private Integer catLevel;
    /**
     * 是否显示[0-不显示，1显示]
     */
	private Integer showStatus;
    /**
     * 排序
     */
	private Integer sort;
    /**
     * 图标地址
     */
	private String icon;
    /**
     * 计量单位
     */
	private String productUnit;
    /**
     * 商品数量
     */
	private Integer productCount;
}