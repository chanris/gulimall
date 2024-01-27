package com.chanris.gulimall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.coupon.dao.HomeSubjectSpuDao;
import com.chanris.gulimall.coupon.dto.HomeSubjectSpuDTO;
import com.chanris.gulimall.coupon.service.HomeSubjectSpuService;
import com.chanris.gulimall.coupon.entity.HomeSubjectSpuEntity;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 专题商品
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class HomeSubjectSpuServiceImpl extends CrudServiceImpl<HomeSubjectSpuDao, HomeSubjectSpuEntity, HomeSubjectSpuDTO> implements HomeSubjectSpuService {

    @Override
    public QueryWrapper<HomeSubjectSpuEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<HomeSubjectSpuEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}