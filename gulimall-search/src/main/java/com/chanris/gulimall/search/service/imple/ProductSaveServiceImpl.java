package com.chanris.gulimall.search.service.imple;

import com.alibaba.fastjson2.JSON;
import com.chanris.gulimall.common.to.es.SkuEsModel;
import com.chanris.gulimall.search.config.ElasticConfig;
import com.chanris.gulimall.search.constant.EsConstant;
import com.chanris.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 28/2/2024
 * @description
 */
@Slf4j
@Service
public class ProductSaveServiceImpl implements ProductSaveService {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
//        log.info("待上架商品列表: {} size: {}", skuEsModels, skuEsModels.size());
        //保存到es
        // 1.给es中建立索引, product
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel model : skuEsModels) {
            IndexRequest request = new IndexRequest(EsConstant.PRODUCT_INDEX);
            request.id(model.getSkuId().toString());
            String s = JSON.toJSONString(model);
            request.source(s, XContentType.JSON);
            log.info("上架商品JSON数据: {}", s);
            bulkRequest.add(request);
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, ElasticConfig.COMMON_OPTIONS);

        // TODO 处理批量错误
        boolean b = bulk.hasFailures();
        List<String> collect = Arrays.stream(bulk.getItems()).filter(BulkItemResponse::isFailed).map(BulkItemResponse::getId).toList();
        if(collect.size() != 0) {
            log.error("商品上架错误: {}", collect);
        }
        return b;
    }
}
