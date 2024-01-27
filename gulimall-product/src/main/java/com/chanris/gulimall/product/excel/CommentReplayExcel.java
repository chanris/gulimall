package com.chanris.gulimall.product.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;

/**
 * 商品评价回复关系
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
public class CommentReplayExcel {
    @ExcelProperty(value = "id")
    private Long id;
    @ExcelProperty(value = "评论id")
    private Long commentId;
    @ExcelProperty(value = "回复id")
    private Long replyId;

}