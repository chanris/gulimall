package com.chanris.gulimall.member.exception;

/**
 * @author chenyue7@foxmail.com
 * @date 7/3/2024
 * @description
 */
public class UsernameExistException extends RuntimeException{
    public UsernameExistException() {
        super("用户名已存在");
    }
}
