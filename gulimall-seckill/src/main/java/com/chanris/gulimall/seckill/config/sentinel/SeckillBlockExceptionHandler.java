package com.chanris.gulimall.seckill.config.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.chanris.gulimall.common.exception.CodeEnum;
import com.chanris.gulimall.common.utils.Result;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/14
 * @description 自定义全局sentinel 异常返回对象
 */
@Component
public class SeckillBlockExceptionHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        Result r = new Result();
        r.setCode(CodeEnum.TOO_MANY_REQUEST.code);
        r.setMsg(CodeEnum.TOO_MANY_REQUEST.msg);
        response.getWriter().println(JSON.toJSONString(r));
    }
}
