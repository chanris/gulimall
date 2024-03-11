package com.chanris.gulimall.auth.controller;

import com.alibaba.fastjson2.JSON;
import com.chanris.gulimall.auth.feign.MemberFeignService;
import com.chanris.gulimall.auth.feign.ThirdPartFeignService;
import com.chanris.gulimall.auth.vo.UserLoginVo;
import com.chanris.gulimall.auth.vo.UserRegisterVo;
import com.chanris.gulimall.common.constant.AuthServerConstant;
import com.chanris.gulimall.common.exception.CodeEnum;
import com.chanris.gulimall.common.utils.Result;
import com.chanris.gulimall.common.vo.MemberResponseVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.chanris.gulimall.common.constant.AuthServerConstant.LOGIN_USER;

/**
 * @author chenyue7@foxmail.com
 * @date 6/3/2024
 * @description
 */
@Controller
public class LoginController {

//    @GetMapping("/login.html")
//    public String loginPage() {
//        return "login";
//    }

//    @GetMapping("/reg.html")
//    public String regPage() {
//        return "reg";
//    }

    @Resource
    private ThirdPartFeignService thirdPartFeignService;

    @Resource
    private MemberFeignService memberFeignService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 1.接口防刷
     * 2.验证码的再次校验
     *
     * @param phone
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/sms/sendCode")
    public Result<?> sendCode(@RequestParam("phone") String phone) {

        //1、接口防刷
        String redisCode = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if (StringUtils.hasLength(redisCode)) {
            //活动存入redis的时间，用当前时间减去存入redis的时间，判断用户手机号是否在60s内发送验证码
            long currentTime = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis() - currentTime < 60000) {
                //60s内不能再发
                return new Result<>().error(CodeEnum.SMS_CODE_EXCEPTION.code, CodeEnum.SMS_CODE_EXCEPTION.msg);
            }
        }

        //2、验证码的再次效验 redis.存key-phone,value-code
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        String codeNum = String.valueOf(code);
        String redisStorage = codeNum + "_" + System.currentTimeMillis();

        //存入redis，防止同一个手机号在60秒内再次发送验证码
        stringRedisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone,
                redisStorage, 10, TimeUnit.MINUTES);
        thirdPartFeignService.sendCode(phone, codeNum);
        return new Result<>();
    }

    /**
     * TODO: 重定向携带数据：利用session原理，将数据放在session中。
     * TODO:只要跳转到下一个页面取出这个数据以后，session里面的数据就会删掉
     * TODO：分布下session问题
     *
     * RedirectAttributes：重定向也可以保留数据，不会丢失
     * 用户注册
     * 不同的域名，不能使用相同的jsessionid
     * Session共享问题解决-hash一致性
     * 优点：
     * 1. 只需要修改nginx，不需要改应用代码
     * 2. 负载均衡；只要hash属性的值分布是均匀的，多台web-server的负载是均衡的
     * 3. 可以支持web-server水平扩展（session同步是不行的，受内存限制）
     * 缺点：
     * 1. session还是存在web-server中的，所有web-server重启可能导致session丢失，影响业务，如部分用户需要重新登录
     * 2. 如果web-server水平扩展，rehash后session重新分布，也会有一部分用户路由不到正确的session
     *
     * session共享问题解决-统一存储
     * 优点
     * 1.没有安全隐患
     * 2.可以水平扩展
     * 3.web-server重启或扩容都不会有session丢失
     * 不足
     * 1. 增加了一次网络调用，并且需要修改应用代码；如将所有的getSession方法替换为从redis中查数据，redis获得数据比内存慢很多
     * 2. 上面的缺点都可以使用SpringSession 完美解决。
     *
     *
     * @return
     */
    @PostMapping(value = "/register")
    public String register(@Valid UserRegisterVo vos, BindingResult result, RedirectAttributes attributes) {
        //如果有错误回到注册页面
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            attributes.addFlashAttribute("errors", errors);

            //效验出错回到注册页面
            return "redirect:http://auth.gulimall.com/reg.html";
        }
        //1、效验验证码
        String code = vos.getCode();
        //获取存入Redis里的验证码
        String redisCode = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vos.getPhone());
        if (StringUtils.hasLength(redisCode)) {
            //截取字符串
            if (code.equals(redisCode.split("_")[0])) {
                //删除验证码;令牌机制
                stringRedisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vos.getPhone());
                //验证码通过，真正注册，调用远程服务进行注册
                Result<?> register = memberFeignService.register(vos);
                if (register.getCode() == 0) {
                    //成功
                    return "redirect:http://auth.gulimall.com/login.html";
                } else {
                    //失败
                    Map<String, String> errors = new HashMap<>();
                    errors.put("msg", JSON.toJSONString(register.getData()));
                    attributes.addFlashAttribute("errors", errors);
                    return "redirect:http://auth.gulimall.com/reg.html";
                }
            } else {
                //效验出错回到注册页面
                Map<String, String> errors = new HashMap<>();
                errors.put("code", "验证码错误");
                attributes.addFlashAttribute("errors", errors);
                return "redirect:http://auth.gulimall.com/reg.html";
            }
        } else {
            //效验出错回到注册页面
            Map<String, String> errors = new HashMap<>();
            errors.put("code", "验证码错误");
            attributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.gulimall.com/reg.html";
        }
    }

    @GetMapping(value = "/login.html")
    public String loginPage(HttpSession session) {

        //从session先取出来用户的信息，判断用户是否已经登录过了
        Object attribute = session.getAttribute(LOGIN_USER);
        //如果用户没登录那就跳转到登录页面
        if (attribute == null) {
            return "login";
        } else {
            return "redirect:http://gulimall.com";
        }
    }

    @PostMapping(value = "/login")
    public String login(UserLoginVo vo, RedirectAttributes attributes, HttpSession session) {

        //远程登录
        Result<MemberResponseVo> login = memberFeignService.login(vo);

        if (login.getCode() == 0) {
            MemberResponseVo data = login.getData();
            session.setAttribute(LOGIN_USER, data);
            return "redirect:http://gulimall.com";
        } else {
            Map<String,String> errors = new HashMap<>();
            errors.put("msg", login.getMsg());
            attributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.gulimall.com/login.html";
        }
    }
}
