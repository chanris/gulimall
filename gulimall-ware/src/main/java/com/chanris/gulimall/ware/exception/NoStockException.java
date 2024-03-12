package com.chanris.gulimall.ware.exception;

/**
 * @author chenyue7@foxmail.com
 * @date 12/3/2024
 * @description
 */
public class NoStockException extends RuntimeException{
    public NoStockException(String msg) {
        super(msg);
    }
}
