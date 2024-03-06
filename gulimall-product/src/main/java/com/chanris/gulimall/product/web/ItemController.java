package com.chanris.gulimall.product.web;

import com.chanris.gulimall.product.service.impl.SkuInfoServiceImpl;
import com.chanris.gulimall.product.vo.SkuItemVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * @author chenyue7@foxmail.com
 * @date 5/3/2024
 * @description
 */
@Controller
public class ItemController {

    @Resource
    private SkuInfoServiceImpl skuInfoService;
    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable Long skuId, Model model) throws ExecutionException, InterruptedException {
        SkuItemVo item = skuInfoService.item(skuId);
        model.addAttribute("item", item);
        return "item";
    }

    @GetMapping("item")
    @ResponseBody
    public SkuItemVo getSkuItemVo(Long skuId) throws ExecutionException, InterruptedException {
        return skuInfoService.item(skuId);
    }
}
