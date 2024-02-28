package com.chanris.gulimall.search.controller;

import com.chanris.gulimall.common.exception.CodeEnum;
import com.chanris.gulimall.common.to.es.SkuEsModel;
import com.chanris.gulimall.common.utils.Result;
import com.chanris.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 28/2/2024
 * @description
 */
@Slf4j
@RequestMapping("/search/save")
@RestController
public class ElasticSaveController {

    @Resource
    private ProductSaveService productSaveService;

    //上架商品
    @PostMapping("product")
    public Result<?> productStatusUp(@RequestBody List<SkuEsModel> skuEsModels) {
        boolean b = false;
        Result<?> result = new Result<>();
        try {
            b = productSaveService.productStatusUp(skuEsModels);
        }catch (Exception e) {
            log.error("ElasticSaveController商品上架错误: ", e);
            result.error(CodeEnum.PRODUCT_UP_EXCEPTION.code, CodeEnum.PRODUCT_UP_EXCEPTION.msg);
        }
        if(b) {
            result.error(CodeEnum.PRODUCT_UP_EXCEPTION.code, CodeEnum.PRODUCT_UP_EXCEPTION.msg);
        }
        return result;
    }
}
