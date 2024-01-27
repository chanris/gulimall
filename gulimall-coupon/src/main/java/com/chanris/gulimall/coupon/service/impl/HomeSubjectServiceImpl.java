package com.chanris.gulimall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.coupon.dao.HomeSubjectDao;
import com.chanris.gulimall.coupon.dto.HomeSubjectDTO;
import com.chanris.gulimall.coupon.service.HomeSubjectService;
import com.chanris.gulimall.coupon.entity.HomeSubjectEntity;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 首页专题表【jd首页下面很多专题，每个专题链接新的页面，展示专题商品信息】
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class HomeSubjectServiceImpl extends CrudServiceImpl<HomeSubjectDao, HomeSubjectEntity, HomeSubjectDTO> implements HomeSubjectService {

    @Override
    public QueryWrapper<HomeSubjectEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<HomeSubjectEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}