package com.chanris.gulimall.product.config.excepiton;

import com.chanris.gulimall.common.exception.CodeEnum;
import com.chanris.gulimall.common.exception.RenException;
import com.chanris.gulimall.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author chenyue7@foxmail.com
 * @date 4/2/2024
 * @description
 */
@Slf4j
@RestControllerAdvice(basePackages = {"com.chanris.gulimall.product.app"})
public class GlobalProductExceptionHandler {


    // 全局异常捕捉
    // 捕捉声明的异常及其子异常
    // 如果显示声明了某子异常就不会在该exceptionHandler处理！！
    // 如果有多个父类exceptionHandler 会选择最近的父类exceptionHandler处理
    @ExceptionHandler(value = {Exception.class})
    public Result defaultExceptionHandle(Exception ex){
        log.warn("===========================捕捉到全局异常=====================================");
        log.warn("异常类型: {}", ex.getClass().toGenericString());
        log.warn("异常信息", ex);
        log.warn("===============================================================================");
        return new Result().error(CodeEnum.INTERNAL_SERVER_ERROR.code, CodeEnum.INTERNAL_SERVER_ERROR.msg + ": " + ex.getMessage());
    }

    @ExceptionHandler(value = {RenException.class})
    public Result parameterExceptionHandle(Exception ex) {
        log.warn("===========================捕捉到请求参数异常===================================");
        log.warn("异常类型: {}", ex.getClass().toGenericString());
        log.warn("异常信息", ex);
        log.warn("=============================================================================");
        return new Result().error(CodeEnum.VALID_EXCEPTION.code, CodeEnum.VALID_EXCEPTION.msg + ": " +ex.getMessage());
    }

    /*@ExceptionHandler(value = {RenException.class})
    public Result businessExceptionHandle(Exception ex){
        log.warn("===========================捕捉到业务异常===========================");
        log.warn("异常类型: {}", ex.getClass().toGenericString());
        log.warn("异常信息：{}", ex.getMessage());
        log.warn("====================================================================");
        return new Result().error(ErrorCode.BAD_REQUEST_PARAMETER, ex.getMessage());
    }*/
}
