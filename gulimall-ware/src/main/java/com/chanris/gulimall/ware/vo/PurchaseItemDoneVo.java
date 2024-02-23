package com.chanris.gulimall.ware.vo;

import lombok.Data;

/**
 * @author chenyue7@foxmail.com
 * @date 23/2/2024
 * @description
 */
@Data
public class PurchaseItemDoneVo {
    private Long itemId;
    private Integer status;
    private String reason;
}
