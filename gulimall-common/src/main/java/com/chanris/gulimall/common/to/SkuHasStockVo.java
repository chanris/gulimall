package com.chanris.gulimall.common.to;

import lombok.Data;

/**
 * @author chenyue7@foxmail.com
 * @date 23/2/2024
 * @description
 */

@Data
public class SkuHasStockVo {
    private Long skuId;
    private Boolean hasStock;
}
