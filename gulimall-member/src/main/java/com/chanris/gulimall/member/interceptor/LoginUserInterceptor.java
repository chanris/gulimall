package com.chanris.gulimall.member.interceptor;

import com.chanris.gulimall.common.constant.AuthServerConstant;
import com.chanris.gulimall.common.vo.MemberResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author chenyue7@foxmail.com
 * @date 11/3/2024
 * @description 请求拦截器
 * 分布式Session登录方案，登录信息保存在Cookie中。
 */
@Slf4j
@Component
public class LoginUserInterceptor implements HandlerInterceptor {
    public static ThreadLocal<MemberResponseVo> loginUser = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        log.info("请求地址：{}", uri);
        log.info("Cookie信息：{}", request.getHeader("Cookie"));
        boolean tryLogin = new AntPathMatcher().match("/member/member/login", uri); // 开放登录接口
        boolean tryLogin2 = new AntPathMatcher().match("/member/member/oauth2/login", uri);
        // 登录请求，不验证
        if (tryLogin || tryLogin2) {
            return true;
        }
        // 从Redis中获得分布式Session信息
        MemberResponseVo attribute = (MemberResponseVo) request.getSession().getAttribute(AuthServerConstant.LOGIN_USER);
        if(attribute != null) {
            loginUser.set(attribute);
            return true;
        }else  {
            request.getSession().setAttribute("msg", "请先登录");
            response.sendRedirect("http://auth.gulimall.com/login.html");
            return false;
        }
    }
}
