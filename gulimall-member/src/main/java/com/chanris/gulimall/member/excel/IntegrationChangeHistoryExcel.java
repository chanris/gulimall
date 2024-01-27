package com.chanris.gulimall.member.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;

/**
 * 积分变化历史记录
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
public class IntegrationChangeHistoryExcel {
    @ExcelProperty(value = "id")
    private Long id;
    @ExcelProperty(value = "member_id")
    private Long memberId;
    @ExcelProperty(value = "create_time")
    private Date createTime;
    @ExcelProperty(value = "变化的值")
    private Integer changeCount;
    @ExcelProperty(value = "备注")
    private String note;
    @ExcelProperty(value = "来源[0->购物；1->管理员修改;2->活动]")
    private Integer sourceTyoe;

}