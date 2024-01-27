package com.chanris.gulimall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.coupon.dao.SeckillSkuRelationDao;
import com.chanris.gulimall.coupon.dto.SeckillSkuRelationDTO;
import com.chanris.gulimall.coupon.service.SeckillSkuRelationService;
import com.chanris.gulimall.coupon.entity.SeckillSkuRelationEntity;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 秒杀活动商品关联
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class SeckillSkuRelationServiceImpl extends CrudServiceImpl<SeckillSkuRelationDao, SeckillSkuRelationEntity, SeckillSkuRelationDTO> implements SeckillSkuRelationService {

    @Override
    public QueryWrapper<SeckillSkuRelationEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SeckillSkuRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}