package com.chanris.gulimall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.coupon.dao.CouponSpuRelationDao;
import com.chanris.gulimall.coupon.dto.CouponSpuRelationDTO;
import com.chanris.gulimall.coupon.service.CouponSpuRelationService;
import com.chanris.gulimall.coupon.entity.CouponSpuRelationEntity;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 优惠券与产品关联
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class CouponSpuRelationServiceImpl extends CrudServiceImpl<CouponSpuRelationDao, CouponSpuRelationEntity, CouponSpuRelationDTO> implements CouponSpuRelationService {

    @Override
    public QueryWrapper<CouponSpuRelationEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<CouponSpuRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}