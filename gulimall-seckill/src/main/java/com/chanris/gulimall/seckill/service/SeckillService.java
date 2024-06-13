package com.chanris.gulimall.seckill.service;

import com.chanris.gulimall.common.to.seckill.SeckillSkuRedisTo;
import com.chanris.gulimall.common.to.seckill.SkuInfoTo;

import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/12
 * @description
 */
public interface SeckillService {

    void uploadSeckillSkuLatest3Days();

    List<SeckillSkuRedisTo> getCurrentSeckillSkus();

    SeckillSkuRedisTo getSkuSeckillInfo(Long skuId);

    String kill(String killId, String key, Integer num);
}
