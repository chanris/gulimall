package com.chanris.gulimall.search.service;

import com.chanris.gulimall.search.vo.SearchParam;
import com.chanris.gulimall.search.vo.SearchResult;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chenyue7@foxmail.com
 * @date 1/3/2024
 * @description
 */
public interface MallSearchService {

    SearchResult search(SearchParam searchParam);
}
