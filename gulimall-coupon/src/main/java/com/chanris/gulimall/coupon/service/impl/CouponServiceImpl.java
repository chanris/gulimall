package com.chanris.gulimall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.coupon.dao.CouponDao;
import com.chanris.gulimall.coupon.dto.CouponDTO;
import com.chanris.gulimall.coupon.service.CouponService;
import com.chanris.gulimall.coupon.entity.CouponEntity;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 优惠券信息
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class CouponServiceImpl extends CrudServiceImpl<CouponDao, CouponEntity, CouponDTO> implements CouponService {

    @Override
    public QueryWrapper<CouponEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<CouponEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}