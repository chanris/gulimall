package com.chanris.gulimall.search.service;

import com.chanris.gulimall.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 28/2/2024
 * @description
 */
public interface ProductSaveService {
    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
