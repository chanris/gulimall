package com.chanris.gulimall.cart.interceptor;

import com.chanris.gulimall.cart.vo.UserInfoTo;
import com.chanris.gulimall.common.constant.AuthServerConstant;
import com.chanris.gulimall.common.constant.CartConstant;
import com.chanris.gulimall.common.vo.MemberResponseVo;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @author chenyue7@foxmail.com
 * @date 9/3/2024
 * @description 在执行方法之前，判断用户的登录状态，并封装传递给controller目标
 */
public class CartInterceptor implements HandlerInterceptor {

    // 线程内的局部变量，仅限于线程内部使用，保证多线程安全
    public static ThreadLocal<UserInfoTo> threadLocal = new InheritableThreadLocal<>();

    /**
     * 目标方法执行之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        UserInfoTo userInfoTo = new UserInfoTo();
        MemberResponseVo attribute = (MemberResponseVo) session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (attribute != null){
            userInfoTo.setUserId(attribute.getId());
        }else {
            Cookie[] cookies = request.getCookies();
            if(cookies != null && cookies.length > 0) {
                for (Cookie c : cookies) {
                    if (c.getName().equals(CartConstant.TEMP_USER_COOKIE)) {
                        userInfoTo.setUserKey(c.getValue());
                        break;
                    }
                }
            }
        }
        // 没有临时用户
        if (userInfoTo.getUserKey() == null) {
            String uuid = UUID.randomUUID().toString();
            userInfoTo.setUserKey(uuid);
            userInfoTo.setTempUser(true);
        }
        threadLocal.set(userInfoTo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (threadLocal.get().isTempUser()) {
            Cookie c = new Cookie(CartConstant.TEMP_USER_COOKIE, threadLocal.get().getUserKey());
            c.setDomain("gulimall.com");
            c.setMaxAge(CartConstant.TEMP_USER_COOKIE_TIMEOUT);
            response.addCookie(c);
        }
    }
}
