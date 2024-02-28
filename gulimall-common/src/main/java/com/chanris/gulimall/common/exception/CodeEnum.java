package com.chanris.gulimall.common.exception;

/**
 * @author chenyue7@foxmail.com
 * @date 5/2/2024
 * @description 系统返回码：前两位代表场景 + 后3位代表业务
 *
 * example:
 * 10: 通用
 *   001: 参数校验格式
 * 11： 商品
 */
public enum CodeEnum {
    INTERNAL_SERVER_ERROR(10000, "系统内部错误"),
    VALID_EXCEPTION(10001, "请求参数校验错误"),
    PRODUCT_UP_EXCEPTION(11000, "商品上架异常");

    public final int code;
    public final String msg;
    CodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
