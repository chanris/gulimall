package com.chanris.gulimall.product.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;

/**
 * 品牌分类关联
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
public class CategoryBrandRelationExcel {
    @ExcelProperty(value = "")
    private Long id;
    @ExcelProperty(value = "品牌id")
    private Long brandId;
    @ExcelProperty(value = "分类id")
    private Long catelogId;
    @ExcelProperty(value = "")
    private String brandName;
    @ExcelProperty(value = "")
    private String catelogName;

}