package com.chanris.gulimall.search.controller;

import com.chanris.gulimall.search.service.MallSearchService;
import com.chanris.gulimall.search.vo.SearchParam;
import com.chanris.gulimall.search.vo.SearchResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author chenyue7@foxmail.com
 * @date 1/3/2024
 * @description
 */
@Controller
public class SearchController {

    @Resource
    private MallSearchService mallSearchService;

    @GetMapping("/search")
    @ResponseBody
    public SearchResult listPageRes(SearchParam searchParam, HttpServletRequest request) {
        searchParam.set_queryString(request.getQueryString());
        // 1. 根据查询参数
        SearchResult result = mallSearchService.search(searchParam);

        return result;
    }

    @GetMapping("/list.html")
    public String listPage(SearchParam searchParam, Model model, HttpServletRequest request) {
        searchParam.set_queryString(request.getQueryString());
        // 1. 根据查询参数
        SearchResult search = mallSearchService.search(searchParam);
        model.addAttribute("result", search);
        return "list";
    }
}
