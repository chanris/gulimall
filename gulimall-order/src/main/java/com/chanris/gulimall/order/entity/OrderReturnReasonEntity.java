package com.chanris.gulimall.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 退货原因
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
@TableName("oms_order_return_reason")
public class OrderReturnReasonEntity {

    /**
     * id
     */
	private Long id;
    /**
     * 退货原因名
     */
	private String name;
    /**
     * 排序
     */
	private Integer sort;
    /**
     * 启用状态
     */
	private Integer status;
    /**
     * create_time
     */
	private Date createTime;
}