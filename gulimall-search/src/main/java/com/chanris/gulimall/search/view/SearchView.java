package com.chanris.gulimall.search.view;

import com.chanris.gulimall.common.to.es.SkuEsModel;
import com.chanris.gulimall.search.vo.SearchResult;
import lombok.Data;

import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 1/3/2024
 * @description
 */
@Data
public class SearchView {
    private List<SkuEsModel> products; // 商品信息
    private Integer pageNum; // 当前页码
    private Long total; // 总记录条数
    private Integer totalPages; // 总页码
    private List<SearchResult.BrandVo> brands; //查询到的品牌信息
    private List<SearchResult.CatalogVo> catalogs; //查询到的分类信息
    private List<SearchResult.AttrVo> attrs; //查询到的属性信息

    @Data
    public static class BrandVo {
        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    @Data
    public static class AttrVo {
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }

    @Data
    public static class CatalogVo {
        private Long catalogId;
        private String catalogName;
    }
}
