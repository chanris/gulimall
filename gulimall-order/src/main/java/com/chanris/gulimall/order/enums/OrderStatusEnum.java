package com.chanris.gulimall.order.enums;

/**
 * @author chenyue7@foxmail.com
 * @date 12/3/2024
 * @description
 */
public enum OrderStatusEnum {
    CREATE_NEW(0, "待发货"),
    PAYED(1, "已付款"),
    SENDED(2, "已发货"),
    RECIEVED(3, "已完成"),
    CANCELED(4, "已取消"),
    SERVICING(5, "售后中"),
    SERVICED(6, "售后完成");
    public final Integer code;
    public final String msg;
    OrderStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
