package com.chanris.gulimall.product.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;

/**
 * sku图片
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Data
public class SkuImagesExcel {
    @ExcelProperty(value = "id")
    private Long id;
    @ExcelProperty(value = "sku_id")
    private Long skuId;
    @ExcelProperty(value = "图片地址")
    private String imgUrl;
    @ExcelProperty(value = "排序")
    private Integer imgSort;
    @ExcelProperty(value = "默认图[0 - 不是默认图，1 - 是默认图]")
    private Integer defaultImg;

}