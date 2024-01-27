package com.chanris.gulimall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.coupon.dao.SpuBoundsDao;
import com.chanris.gulimall.coupon.dto.SpuBoundsDTO;
import com.chanris.gulimall.coupon.service.SpuBoundsService;
import com.chanris.gulimall.coupon.entity.SpuBoundsEntity;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 商品spu积分设置
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class SpuBoundsServiceImpl extends CrudServiceImpl<SpuBoundsDao, SpuBoundsEntity, SpuBoundsDTO> implements SpuBoundsService {

    @Override
    public QueryWrapper<SpuBoundsEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SpuBoundsEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}