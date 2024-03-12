package com.chanris.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 12/3/2024
 * @description
 */
@Data
public class WareSkuLockVo {
    private String orderSn;
    private List<OrderItemVo> locks;
}
