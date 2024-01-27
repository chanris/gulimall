package com.chanris.gulimall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chanris.gulimall.common.service.impl.CrudServiceImpl;
import com.chanris.gulimall.coupon.dao.SeckillSkuNoticeDao;
import com.chanris.gulimall.coupon.dto.SeckillSkuNoticeDTO;
import com.chanris.gulimall.coupon.service.SeckillSkuNoticeService;
import com.chanris.gulimall.coupon.entity.SeckillSkuNoticeEntity;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 秒杀商品通知订阅
 *
 * @author Chen Yue chenyue7@foxmail.com
 * @since 1.0.0 2024-01-27
 */
@Service
public class SeckillSkuNoticeServiceImpl extends CrudServiceImpl<SeckillSkuNoticeDao, SeckillSkuNoticeEntity, SeckillSkuNoticeDTO> implements SeckillSkuNoticeService {

    @Override
    public QueryWrapper<SeckillSkuNoticeEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SeckillSkuNoticeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}