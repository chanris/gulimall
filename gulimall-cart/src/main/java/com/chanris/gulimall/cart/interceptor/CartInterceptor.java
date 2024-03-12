package com.chanris.gulimall.cart.interceptor;

import com.chanris.gulimall.cart.vo.UserInfoTo;
import com.chanris.gulimall.common.constant.AuthServerConstant;
import com.chanris.gulimall.common.constant.CartConstant;
import com.chanris.gulimall.common.vo.MemberResponseVo;
import org.springframework.util.StringUtils;
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
        UserInfoTo userInfoTo = new UserInfoTo();

        HttpSession session = request.getSession();
        //获得当前登录用户的信息
        MemberResponseVo memberResponseVo = (MemberResponseVo) session.getAttribute(AuthServerConstant.LOGIN_USER);

        if (memberResponseVo != null) {
            //用户登录了
            userInfoTo.setUserId(memberResponseVo.getId());
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                //user-key
                String name = cookie.getName();
                if (name.equals(CartConstant.TEMP_USER_COOKIE)) {
                    userInfoTo.setUserKey(cookie.getValue());
                    //标记为已是临时用户
                    userInfoTo.setTempUser(true);
                }
            }
        }

        //如果没有临时用户一定分配一个临时用户
        if (!StringUtils.hasLength(userInfoTo.getUserKey())) {
            String uuid = UUID.randomUUID().toString();
            userInfoTo.setUserKey(uuid);
        }

        //目标方法执行之前
        threadLocal.set(userInfoTo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 如果没有临时用户，创建一个临时用户user-key
        if (!threadLocal.get().isTempUser()) {
            Cookie c = new Cookie(CartConstant.TEMP_USER_COOKIE, threadLocal.get().getUserKey());
            c.setDomain("gulimall.com");
            c.setMaxAge(CartConstant.TEMP_USER_COOKIE_TIMEOUT);
            response.addCookie(c);
        }
    }
}
