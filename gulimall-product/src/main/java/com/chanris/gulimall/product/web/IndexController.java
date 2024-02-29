package com.chanris.gulimall.product.web;

import com.chanris.gulimall.product.entity.CategoryEntity;
import com.chanris.gulimall.product.service.CategoryService;
import com.chanris.gulimall.product.vo.Catelog2Vo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author chenyue7@foxmail.com
 * @date 28/2/2024
 * @description
 */
@Controller
public class IndexController {

    @Resource
    private CategoryService categoryService;

    @GetMapping(value = {"/", "/index.html"})
    public String index(Model model) {
        // 1.查出所有的一级分类
        List<CategoryEntity> categoryEntityList = categoryService.getTopLevelCategoryList();
        // 视图解析器进行拼接： classpath:/templates/ + 返回值 + .html
        model.addAttribute("categories", categoryEntityList);
        return "index";
    }

    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatalogView() {
        Map<String, List<Catelog2Vo>> catalogView = categoryService.getCatalogView();
        return catalogView;
    }
}
