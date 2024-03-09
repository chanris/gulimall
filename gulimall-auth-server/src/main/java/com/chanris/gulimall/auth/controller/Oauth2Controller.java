package com.chanris.gulimall.auth.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.chanris.gulimall.auth.component.GiteeHttpClient;
import com.chanris.gulimall.auth.feign.MemberFeignService;
import com.chanris.gulimall.auth.vo.SocialUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * @author chenyue7@foxmail.com
 * @date 7/3/2024
 * @description
 */
@Controller
public class Oauth2Controller {
    /**
     * gitee授权中提供的 appid 和 appkey
     */
    @Value("${gitee.oauth.clientid}")
    public String CLIENTID;
    @Value("${gitee.oauth.clientsecret}")
    public String CLIENTSECRET;
    @Value("${gitee.oauth.callback}")
    public String URL;
    @Resource
    private MemberFeignService memberFeignService;

    /**
     * 请求授权页面
     */
    @GetMapping(value = "/auth")
    public String giteeAuth(HttpSession session) {
        // 用于第三方应用防止CSRF攻击
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        session.setAttribute("state", uuid);

        // Step1：获取Authorization Code
        String url = "https://gitee.com/oauth/authorize?response_type=code" +
                "&client_id=" + CLIENTID +
                "&redirect_uri=" + URLEncoder.encode(URL) +
                "&state=" + uuid +
                "&scope=user_info";
        //因为使用的是thymeleaf模板引擎，所以是无法解析一个网址的，只能重定向
        return "redirect:"+url;
    }
    /**
     * 授权回调
     * 获得授权码，
     * 通过 授权码 http请求 gitee 的 access token （授权码 只能使用一次，access token 可以使用多次）
     */
    @GetMapping(value = "/callback")
    public String qqCallback(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        // 得到Authorization Code
        String code = request.getParameter("code");
        // 我们放在地址中的状态码
        String state = request.getParameter("state");
        String uuid = (String) session.getAttribute("state");

        // 验证信息我们发送的状态码
        if (null != uuid) {
            // 状态码不正确，直接返回登录页面
            if (!uuid.equals(state)) {
                return "redirect:http://auth.gulimall.com/login.html";
            }
        }

        // Step2：通过Authorization Code获取Access Token
        String url = "https://gitee.com/oauth/token?grant_type=authorization_code" +
                "&client_id=" + CLIENTID +
                "&client_secret=" + CLIENTSECRET +
                "&code=" + code +
                "&redirect_uri=" + URL;
        JSONObject accessTokenJson = GiteeHttpClient.getAccessToken(url);
        System.out.println("accessTokenJson:" + accessTokenJson);
        SocialUser socialUser = new SocialUser();
        assert accessTokenJson != null;
        socialUser.setAccess_token(accessTokenJson.getString("access_token"));
        socialUser.setExpires_in(accessTokenJson.getLong("expires_in"));

        // Step3: 获取用户信息
        url = "https://gitee.com/api/v5/user?access_token=" + accessTokenJson.get("access_token");
        JSONObject jsonObject = GiteeHttpClient.getUserInfo(url);
        socialUser.setUid(jsonObject.getString("id"));
        /**
         * 获取到用户信息之后，就该写你自己的业务逻辑了
         */
        System.out.println(JSON.toJSONString(jsonObject));

        memberFeignService.oauthLogin(socialUser);
        return "redirect:http://gulimall.com";
    }
}
