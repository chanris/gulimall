package com.chanris.gulimall.cart.exception;

import com.chanris.gulimall.common.exception.CodeEnum;
import com.chanris.gulimall.common.utils.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chenyue7@foxmail.com
 * @date 11/3/2024
 * @description
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 全局统一异常处理
     * @param exception
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Result<?> handler(RuntimeException exception) {
        return new Result<>().error(CodeEnum.INTERNAL_SERVER_ERROR.code, CodeEnum.INTERNAL_SERVER_ERROR.msg);
    }

    @ExceptionHandler(CartException.class)
    public Result<?> userHandler(CartException exception) {
        return new Result<>().error("购物车无此商品: " + exception.getMessage());
    }
}
