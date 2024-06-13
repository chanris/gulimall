package com.chanris.gulimall.common.to.seckill;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/12
 * @description
 */
@Data
public class SeckillSkuRelationTo {
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
}
