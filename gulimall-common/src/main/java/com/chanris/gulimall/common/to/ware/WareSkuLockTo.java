package com.chanris.gulimall.common.to.ware;

import lombok.Data;

import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 12/3/2024
 * @description
 */
@Data
public class WareSkuLockTo {
    private String orderSn;
    private List<OrderItemTo> locks;
}
