package com.chanris.gulimall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.coupon.dao.HomeAdvDao;
import com.chanris.gulimall.coupon.dto.HomeAdvDTO;
import com.chanris.gulimall.coupon.service.HomeAdvService;
import com.chanris.gulimall.coupon.entity.HomeAdvEntity;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 首页轮播广告
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class HomeAdvServiceImpl extends CrudServiceImpl<HomeAdvDao, HomeAdvEntity, HomeAdvDTO> implements HomeAdvService {

    @Override
    public QueryWrapper<HomeAdvEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<HomeAdvEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}