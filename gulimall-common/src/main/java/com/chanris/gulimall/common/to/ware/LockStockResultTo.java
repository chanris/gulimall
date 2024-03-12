package com.chanris.gulimall.common.to.ware;

import lombok.Data;

/**
 * @author chenyue7@foxmail.com
 * @date 12/3/2024
 * @description
 */
@Data
public class LockStockResultTo {
    private Long skuId;
    private Integer num;
    private Boolean locked;
}
