package top.codecrab.gulimall.auth.controller;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.codecrab.common.constant.AuthConstant;
import top.codecrab.common.constant.ThirdPartyConstant;
import top.codecrab.common.response.ErrorCodeEnum;
import top.codecrab.common.response.R;
import top.codecrab.common.vo.MemberRespVo;
import top.codecrab.gulimall.auth.client.MemberFeignClient;
import top.codecrab.gulimall.auth.client.ThirdPartyFeignClient;
import top.codecrab.gulimall.auth.vo.LoginVo;
import top.codecrab.gulimall.auth.vo.RegisterVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author codecrab
 * @since 2021年06月21日 8:48
 */
@Slf4j
@Controller
public class AuthController {

    @Resource
    private ThirdPartyFeignClient thirdPartyFeignClient;

    @Resource
    private MemberFeignClient memberFeignClient;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @ResponseBody
    @GetMapping("/auth/send-code")
    public R sendCode(@RequestParam String phone) {
        if (!PhoneUtil.isMobile(phone)) {
            return R.error(ErrorCodeEnum.PHONE_IS_NOT_LEGAL);
        }

        BoundValueOperations<String, Object> ops = redisTemplate.boundValueOps(AuthConstant.CODE + phone);

        Object o = ops.get();
        if (o != null) {
            String[] split = o.toString().split("_");
            long l = Long.parseLong(split[1]);
            if (System.currentTimeMillis() - l < 60000) {
                return R.error(ErrorCodeEnum.ALIYUN_SMS_LIMIT_CONTROL_ERROR);
            }
        }

        String numbers = RandomUtil.randomNumbers(6);
        ops.set(numbers + "_" + System.currentTimeMillis(), ThirdPartyConstant.SMS.EXPIRE_DATE, TimeUnit.MINUTES);
        //{"code": "111111","phone": "phone","minute": 5,"templateCode": "xxx","useAccess": "true"}
        Map<String, Object> param = new HashMap<>();
        param.put("code", numbers);
        param.put("phone", phone);
        thirdPartyFeignClient.sendCode(param);
        return R.ok();
    }

    /**
     * @param attributes 重定向可以携带数据，实现原理是 session
     */
    @PostMapping("/register")
    public String register(
            @Valid RegisterVo vo,
            BindingResult result,
            RedirectAttributes attributes
    ) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors()
                    .stream().collect(Collectors.toMap(
                            FieldError::getField,
                            val -> val.getDefaultMessage() == null ? "" : val.getDefaultMessage()
                    ));
            //addFlashAttribute只能取出一次
            attributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.gulimall.com/reg.html";
        }

        BoundValueOperations<String, Object> ops = redisTemplate.boundValueOps(AuthConstant.CODE + vo.getPhone());
        Object o = ops.get();
        //value格式：验证码_时间戳
        if (o == null || !vo.getCode().equals(o.toString().split("_")[0])) {
            attributes.addFlashAttribute("errors", MapUtil.of("code", "验证码错误"));
            return "redirect:http://auth.gulimall.com/reg.html";
        }

        if (!vo.getPassword().equals(vo.getRePass())) {
            attributes.addFlashAttribute("errors", MapUtil.of("rePass", "两次密码不一致"));
            return "redirect:http://auth.gulimall.com/reg.html";
        }

        //远程调用会员服务，注册用户
        try {
            //删除redis的验证码，令牌机制
            redisTemplate.delete(AuthConstant.CODE + vo.getPhone());

            R r = memberFeignClient.register(vo);
            if (r.getCode() == 0) {
                //成功
                return "redirect:http://auth.gulimall.com/login.html";
            } else {
                //失败
                attributes.addFlashAttribute("errors", MapUtil.of("errorMsg", r.getMsg()));
                return "redirect:http://auth.gulimall.com/reg.html";
            }
        } catch (Exception e) {
            log.error("远程调用memberFeignClient.register异常", e);
            attributes.addFlashAttribute("errors", MapUtil.of("errorMsg", "服务器未知异常"));
            return "redirect:http://auth.gulimall.com/reg.html";
        }
    }

    @PostMapping("/login")
    public String login(LoginVo vo, RedirectAttributes attributes, HttpSession session) {
        try {
            R r = memberFeignClient.login(vo);
            if (r.getCode() == 0) {
                MemberRespVo data = r.getFeignData(new TypeReference<MemberRespVo>() {
                });
                session.setAttribute(AuthConstant.SESSION_LOGIN_USER, data);
                return "redirect:http://www.gulimall.com";
            }
            attributes.addFlashAttribute("errors", MapUtil.of("errorMsg", r.getMsg()));
            return "redirect:http://auth.gulimall.com/login.html";
        } catch (Exception e) {
            log.error("远程调用memberFeignClient.login异常", e);
            attributes.addFlashAttribute("errors", MapUtil.of("errorMsg", "服务器未知异常"));
            return "redirect:http://auth.gulimall.com/login.html";
        }
    }

    @GetMapping("/login.html")
    public String loginPage(HttpSession session) {
        if (session.getAttribute(AuthConstant.SESSION_LOGIN_USER) != null) {
            return "redirect:http://www.gulimall.com";
        }
        return "login";
    }
}
