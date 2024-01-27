package com.chanris.gulimall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.coupon.dao.SeckillPromotionDao;
import com.chanris.gulimall.coupon.dto.SeckillPromotionDTO;
import com.chanris.gulimall.coupon.service.SeckillPromotionService;
import com.chanris.gulimall.coupon.entity.SeckillPromotionEntity;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 秒杀活动
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class SeckillPromotionServiceImpl extends CrudServiceImpl<SeckillPromotionDao, SeckillPromotionEntity, SeckillPromotionDTO> implements SeckillPromotionService {

    @Override
    public QueryWrapper<SeckillPromotionEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SeckillPromotionEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}