package com.chanris.gulimall.order.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 退货原因
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
public class OrderReturnReasonExcel {
    @ExcelProperty(value = "id")
    private Long id;
    @ExcelProperty(value = "退货原因名")
    private String name;
    @ExcelProperty(value = "排序")
    private Integer sort;
    @ExcelProperty(value = "启用状态")
    private Integer status;
    @ExcelProperty(value = "create_time")
    private Date createTime;

}