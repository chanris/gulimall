package com.chanris.gulimall.order.enums;

/**
 * @author chenyue7@foxmail.com
 * @date 12/3/2024
 * @description
 */
public enum OrderStatusEnum {
    CREATE_NEW(1, "待发货");
    public final Integer code;
    public final String msg;
    OrderStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
