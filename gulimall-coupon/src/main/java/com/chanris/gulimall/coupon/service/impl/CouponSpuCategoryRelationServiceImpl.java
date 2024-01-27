package com.chanris.gulimall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.coupon.dao.CouponSpuCategoryRelationDao;
import com.chanris.gulimall.coupon.dto.CouponSpuCategoryRelationDTO;
import com.chanris.gulimall.coupon.service.CouponSpuCategoryRelationService;
import com.chanris.gulimall.coupon.entity.CouponSpuCategoryRelationEntity;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 优惠券分类关联
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class CouponSpuCategoryRelationServiceImpl extends CrudServiceImpl<CouponSpuCategoryRelationDao, CouponSpuCategoryRelationEntity, CouponSpuCategoryRelationDTO> implements CouponSpuCategoryRelationService {

    @Override
    public QueryWrapper<CouponSpuCategoryRelationEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<CouponSpuCategoryRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}