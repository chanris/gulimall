package com.chanris.gulimall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 1/3/2024
 * @description
 */
@Data
public class SearchParam {

    // 全文检索 skuTitle
    private String keyword;
    //三级分类 id
    private Long catalog3Id;
    // 排序条件 saleCount_asc、saleCount_desc、hotScore_asc、hotScore_desc、skuPrice_asc、skuPrice_desc、
    private String sort;
    //过滤条件
    // 是否有货 0/1
    private Boolean hasStock = true;
    // 价格区间 1_500/ _500 / 500_
    private String skuPrice;
    // 品牌 可多选 ?brandId=1&brandId=2
    private List<Long> brandId;
    private List<String> attrs;
    private Integer pageNum = 1; // 页码
    private Integer pageSize;

    private String _queryString; //原生的所有查询条件
}
