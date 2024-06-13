package com.chanris.gulimall.common.to.seckill;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/12
 * @description 秒杀商品详细信息
 */
@Data
public class SeckillSkuRedisTo {
    /**
     * id
     */
    private Long id;
    /**
     * 活动id
     */
    private Long promotionId;
    /**
     * 活动场次id
     */
    private Long promotionSessionId;
    /**
     * 商品id
     */
    private Long skuId;
    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;
    /**
     * 秒杀总量
     */
    private BigDecimal seckillCount;
    /**
     * 每人限购数量
     */
    private BigDecimal seckillLimit;
    /**
     * 排序
     */
    private Integer seckillSort;

    // 商品基本信息
    private SkuInfoTo skuInfoTo;
    // 当前商品秒杀的开始时间
    private Long startTime;
    // 当前商品秒杀的结束时间
    private Long endTime;

    //商品的秒杀随机码
    private String randomCode;
}
