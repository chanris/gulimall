package com.chanris.gulimall.ware.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 库存工作单
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wms_ware_order_task_detail")
public class WareOrderTaskDetailEntity {

    /**
     * id
     */
	private Long id;
    /**
     * sku_id
     */
	private Long skuId;
    /**
     * sku_name
     */
	private String skuName;
    /**
     * 购买个数
     */
	private Integer skuNum;
    /**
     * 工作单id
     */
	private Long taskId;
    private Long wareId;
    private Integer lockStatus;
}