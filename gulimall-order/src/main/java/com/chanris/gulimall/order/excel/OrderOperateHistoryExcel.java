package com.chanris.gulimall.order.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 订单操作历史记录
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
public class OrderOperateHistoryExcel {
    @ExcelProperty(value = "id")
    private Long id;
    @ExcelProperty(value = "订单id")
    private Long orderId;
    @ExcelProperty(value = "操作人[用户；系统；后台管理员]")
    private String operateMan;
    @ExcelProperty(value = "操作时间")
    private Date createTime;
    @ExcelProperty(value = "订单状态【0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单】")
    private Integer orderStatus;
    @ExcelProperty(value = "备注")
    private String note;

}