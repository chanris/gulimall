package com.chanris.gulimall.member.exception;

/**
 * @author chenyue7@foxmail.com
 * @date 7/3/2024
 * @description
 */
public class PhoneExistException extends RuntimeException {
    public PhoneExistException() {
        super("手机号已存在");
    }
}
