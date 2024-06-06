package com.chanris.gulimall.ware.intercptor;

import com.chanris.gulimall.common.constant.AuthServerConstant;
import com.chanris.gulimall.common.vo.MemberResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author chenyue7@foxmail.com
 * @date 31/5/2024
 * @description
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    public static ThreadLocal<MemberResponseVo> loginUser = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("请求地址： {}", request.getRequestURI());
        log.info("请求头信息: {}", request.getHeader("Cookie"));

        MemberResponseVo attribute = (MemberResponseVo) request.getSession().getAttribute(AuthServerConstant.LOGIN_USER);
        if (attribute != null) {
            loginUser.set(attribute);
            return true;
        }else {
            request.getSession().setAttribute("msg", "请先登录");
            response.sendRedirect("http://auth.gulimall.com/login.html");
            return false;
        }

    }
}
