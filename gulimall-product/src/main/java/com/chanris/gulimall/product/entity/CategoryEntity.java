package com.chanris.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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

    // 坑点!!: 使用mybatis plus的curd功能，如果 id字段不加@TableId 会报错
    // org.apache.ibatis.binding.BindingException: Invalid bound statement (not found): com.********Mybatis.deleteBatchIds
    /**
     * 分类id
     */
    @TableId
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
    //@TableLogic(value = "1", delval = "0")// 逻辑删除字段标记
	@TableLogic
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