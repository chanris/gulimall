package com.chanris.gulimall.cart.exception;

/**
 * @author chenyue7@foxmail.com
 * @date 11/3/2024
 * @description
 */
public class CartException extends RuntimeException{
    public CartException() {
        super("购物车异常");
    }

    public CartException(String msg) {
        super(msg);
    }
}
