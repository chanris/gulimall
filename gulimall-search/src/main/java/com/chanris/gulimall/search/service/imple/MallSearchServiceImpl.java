package com.chanris.gulimall.search.service.imple;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.chanris.gulimall.common.to.es.SkuEsModel;
import com.chanris.gulimall.common.to.product.AttrTo;
import com.chanris.gulimall.common.utils.Result;
import com.chanris.gulimall.search.config.ElasticConfig;
import com.chanris.gulimall.search.constant.EsConstant;
import com.chanris.gulimall.search.feign.ProductFeignService;
import com.chanris.gulimall.search.service.MallSearchService;
import com.chanris.gulimall.search.vo.SearchParam;
import com.chanris.gulimall.search.vo.SearchResult;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.ParsedAggregation;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenyue7@foxmail.com
 * @date 1/3/2024
 * @description
 */
@Service
public class MallSearchServiceImpl implements MallSearchService {

    @Resource
    private RestHighLevelClient esClient;

    @Resource
    private ProductFeignService productFeignService;

    /**
     * 根据检索参数 获得 结果
     * 检索参数： keyword、
     *
     * @param searchParam
     * @return
     */
    @Override
    public SearchResult search(SearchParam searchParam) {
        SearchResult searchResult = null;

        // 1.构建检索请求
        SearchRequest searchRequest = buildSearchRequest(searchParam);

        try {
            SearchResponse searchResponse = esClient.search(searchRequest, ElasticConfig.COMMON_OPTIONS);
            searchResult = buildSearchResult(searchParam, searchResponse);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return searchResult;
    }

    /**
     * 准备检索请求
     * 模糊匹配 过滤(按属性，分类，品牌、价格区间、库存)，排序、分页、高亮、聚合分析
     *
     * @param searchRequest
     */
    private SearchRequest buildSearchRequest(SearchParam searchParam) {

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        /**
         *  查询：模糊匹配，过滤(按照属性、分类、品牌、价格区间、库存)
         */
        // bool - query
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //模糊匹配: must
        if (StringUtils.hasLength(searchParam.getKeyword())) {
            boolQuery.must(QueryBuilders.matchQuery("skuTitle", searchParam.getKeyword()));
        }
        // filter - 按照三级分类id查询
        if (searchParam.getCatalog3Id() != null) {
            boolQuery.filter(QueryBuilders.termQuery("catalogId", searchParam.getCatalog3Id()));
        }
        // filter - 按照品牌id查询
        if (searchParam.getBrandId() != null && searchParam.getBrandId().size() > 0) {
            boolQuery.filter(QueryBuilders.termsQuery("brandId", searchParam.getBrandId()));
        }
        // filter - 按照库存进行查询
        if (searchParam.getHasStock() != null) {
            boolQuery.filter(QueryBuilders.termQuery("hasStock", searchParam.getHasStock()));
        }
        // filter - 按照价格区间
        if (StringUtils.hasLength(searchParam.getSkuPrice())) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("skuPrice");
            String[] s = searchParam.getSkuPrice().split("_");
            /**
             * _600 => "","600"
             * 1_500  => "1","500"
             * 500_ ==> "500"
             */
            if (s.length == 1) {
                rangeQueryBuilder.gte(s[0]);
            } else if (s.length == 2) {
                if (s[0].equals("") && !s[1].equals("")) {
                    rangeQueryBuilder.lte(s[1]).gte("0");
                } else {
                    rangeQueryBuilder.gte(s[0]).lte(s[1]);
                }
            }

           /* if(s.length == 2) {
                rangeQueryBuilder.gte(s[0]).lte(s[1]);
            }else if (s.length == 1) {
                if (searchParam.getSkuPrice().startsWith("_")) {
                    rangeQueryBuilder.lte(s[0]);
                }else if(searchParam.getSkuPrice().endsWith("_")) {
                    rangeQueryBuilder.gte(s[0]);
                }
            }*/
            boolQuery.filter(rangeQueryBuilder);
        }
        // filter - 按照属性进行查询
        if (searchParam.getAttrs() != null && searchParam.getAttrs().size() > 0) {
            // attrs=1_5寸:8寸&attrs=2_16G:8G
            for (String attrStr : searchParam.getAttrs()) {
                BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
                // attr = 1_5寸:8寸
                String[] s = attrStr.split("_");
                String attrId = s[0];
                String[] attrValues = s[1].split(":");
                nestedBoolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId)).must(QueryBuilders.termQuery("attrs.attrValue", attrValues));
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestedBoolQuery, ScoreMode.None);
                boolQuery.filter(nestedQuery);
            }

        }
        sourceBuilder.query(boolQuery);
        /**
         * 排序
         */
        if (StringUtils.hasLength(searchParam.getSort())) {
            String sort = searchParam.getSort();
            sourceBuilder.from((searchParam.getPageNum() - 1) * EsConstant.PRODUCT_PAGESIZE);
            sourceBuilder.size(EsConstant.PRODUCT_PAGESIZE);
        }

        /**
         * 高亮
         */
        if (StringUtils.hasLength(searchParam.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color: red'>");
            highlightBuilder.postTags("</b>");
            sourceBuilder.highlighter(highlightBuilder);
        }

        /**
         * 聚合分析
         */
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg");
        brand_agg.field("brandId").size(50);

        //品牌聚合的子聚合
        brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName.keyword")).size();
        brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg.keyword")).size();
        sourceBuilder.aggregation(brand_agg);

        // 分类聚合
        TermsAggregationBuilder catalog_agg = AggregationBuilders.terms("catalog_agg");
        catalog_agg.field("catalogId").size(50);

        catalog_agg.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName.keyword")).size();
        sourceBuilder.aggregation(catalog_agg);

        //属性聚合
        // 按照属性信息进行聚合
        NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrs");
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        attr_agg.subAggregation(attr_id_agg);
        //2.1.1 在每个属性ID下，按照属性名进行聚合
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName.keyword").size(1));
        //2.1.1 在每个属性ID下，按照属性值进行聚合
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue.keyword").size(50));

        sourceBuilder.aggregation(attr_agg);


//        System.out.println(sourceBuilder.toString());
        SearchRequest request = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, sourceBuilder);
        return request;
    }


    /**
     * 构建检索结果
     *
     * @param searchResult
     */
    private SearchResult buildSearchResult(SearchParam searchParam, SearchResponse searchResponse) {
//        System.out.println( JSON.toJSONString(searchResponse));
        // TODO 4/3/24 这样获得数据太繁琐了，需要重新设计ES DSL语句
        SearchResult result = new SearchResult();
        //1.返回的所有查询到的商品
        SearchHits hits = searchResponse.getHits();
        if(hits.getHits() != null) {
            List<SkuEsModel> skuEsModels = new ArrayList<>(hits.getHits().length);
            for (SearchHit hit : hits.getHits()) {
                String json = hit.getSourceAsString();
                SkuEsModel skuEsModel = JSONObject.parseObject(json, SkuEsModel.class);
                //判断是否按关键字检索，若是就显示高亮，否则不显示
                if (StringUtils.hasLength(searchParam.getKeyword())) {
                    //拿到高亮信息显示标题
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    String skuTitleValue = skuTitle.getFragments()[0].string();
                    skuEsModel.setSkuTitle(skuTitleValue);
                }
                skuEsModels.add(skuEsModel);
            }
            result.setProducts(skuEsModels);
        }

        // 2.当前所有商品设计到的所有分类信息
        ParsedLongTerms catalogAgg = searchResponse.getAggregations().get("catalog_agg");
        if (catalogAgg != null) {
            List<? extends Terms.Bucket> catalogAggBuckets = catalogAgg.getBuckets();
            List<SearchResult.CatalogVo> catalogVos = catalogAggBuckets.stream().map(item->{
                SearchResult.CatalogVo v = new SearchResult.CatalogVo();
                //分类id
                v.setCatalogId(Long.parseLong(item.getKeyAsString()));

                //子聚合
                ParsedStringTerms catalog_name_agg =  item.getAggregations().get("catalog_name_agg");
                String catalogName = catalog_name_agg.getBuckets().get(0).getKeyAsString();
                v.setCatalogName(catalogName);
                return v;
            }).toList();
            result.setCatalogs(catalogVos);
        }

        // 3.当前所有商品的所有品牌信息
        ParsedLongTerms brand_agg =  searchResponse.getAggregations().get("brand_agg");
        if(brand_agg != null) {
            List<SearchResult.BrandVo> brandVos = new ArrayList<>();
            for (Terms.Bucket bucket : brand_agg.getBuckets()) {
                SearchResult.BrandVo brandVo = new SearchResult.BrandVo();

                //1、得到品牌的id
                long brandId = bucket.getKeyAsNumber().longValue();
                brandVo.setBrandId(brandId);

                //2、得到品牌的名字
                ParsedStringTerms brandNameAgg = bucket.getAggregations().get("brand_name_agg");
                String brandName = brandNameAgg.getBuckets().get(0).getKeyAsString();
                brandVo.setBrandName(brandName);

                //3、得到品牌的图片
                ParsedStringTerms brandImgAgg = bucket.getAggregations().get("brand_img_agg");
                String brandImg = brandImgAgg.getBuckets().get(0).getKeyAsString();
                brandVo.setBrandImg(brandImg);

                brandVos.add(brandVo);
            }
            result.setBrands(brandVos);
        }


        // 4.当前所有商品的属性信息
        //获取属性信息的聚合
        ParsedNested attrsAgg = searchResponse.getAggregations().get("attr_agg");
        if(attrsAgg != null) {
            List<SearchResult.AttrVo> attrVos = new ArrayList<>();
            ParsedLongTerms attrIdAgg = attrsAgg.getAggregations().get("attr_id_agg");
            for (Terms.Bucket bucket : attrIdAgg.getBuckets()) {
                SearchResult.AttrVo attrVo = new SearchResult.AttrVo();
                //1、得到属性的id
                long attrId = bucket.getKeyAsNumber().longValue();
                attrVo.setAttrId(attrId);

                //2、得到属性的名字
                ParsedStringTerms attrNameAgg = bucket.getAggregations().get("attr_name_agg");
                String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
                attrVo.setAttrName(attrName);

                //3、得到属性的所有值
                ParsedStringTerms attrValueAgg = bucket.getAggregations().get("attr_value_agg");
                List<String> attrValues = attrValueAgg.getBuckets().stream().map(item -> item.getKeyAsString()).collect(Collectors.toList());
                attrVo.setAttrValue(attrValues);
                attrVos.add(attrVo);
            }
            result.setAttrs(attrVos);
        }



        // 5.分页信息
        long total = searchResponse.getHits().getTotalHits().value;
        result.setPageNum(searchParam.getPageNum());
        result.setTotal(total);
        // 6.分页信息-总页码
        int totalPages = (int) (total % EsConstant.PRODUCT_PAGESIZE == 0 ? total / EsConstant.PRODUCT_PAGESIZE : (total / EsConstant.PRODUCT_PAGESIZE + 1));
        result.setTotalPages(totalPages);

        List<Integer> pageNavs = new ArrayList<>();
        for (int i = 1; i <=  totalPages; i++) {
            pageNavs.add(i);
        }
        result.setPageNavs(pageNavs);

        // 面包屑导航
        if (searchParam.getAttrs() != null && searchParam.getAttrs().size() > 0) {
            List<SearchResult.NavVo> collect = searchParam.getAttrs().stream().map(attr -> {
                //1、分析每一个attrs传过来的参数值
                SearchResult.NavVo navVo = new SearchResult.NavVo();
                String[] s = attr.split("_");
                navVo.setNavValue(s[1]);
                Result<AttrTo> r = productFeignService.get(Long.parseLong(s[0]));
                if (r.getCode() == 0) {
                    navVo.setNavName(r.getData().getAttrName());
                } else {
                    navVo.setNavName(s[0]);
                }

                //2、取消了这个面包屑以后，我们要跳转到哪个地方，将请求的地址url里面的当前置空
                //拿到所有的查询条件，去掉当前
                String encode = null;
                encode = URLEncoder.encode(attr, StandardCharsets.UTF_8);
                encode = encode.replace("+","%20");  //浏览器对空格的编码和Java不一样，差异化处理
                String replace = searchParam.get_queryString().replace("&attrs=" + attr, "");
                navVo.setLink("http://search.gulimall.com/list.html?" + replace);

                return navVo;
            }).collect(Collectors.toList());
            result.setNavs(collect);
        }


        return result;
    }
}
