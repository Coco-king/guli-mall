package top.codecrab.gulimall.auth.controller;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.codecrab.common.constant.OAuth2Constant;
import top.codecrab.common.constant.RedisConstant;
import top.codecrab.common.response.R;
import top.codecrab.common.vo.MemberRespVo;
import top.codecrab.gulimall.auth.client.MemberFeignClient;
import top.codecrab.gulimall.auth.config.OAuth2Properties;
import top.codecrab.gulimall.auth.vo.SocialUserVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author codecrab
 * @since 2021年06月22日 10:02
 */
@Slf4j
@Controller
public class OAuth2Controller {

    @Resource
    private OAuth2Properties oAuth2Properties;

    @Resource
    private MemberFeignClient memberFeignClient;

    @GetMapping("/oauth2.0/weibo/success")
    public String weiBo(@RequestParam String code, HttpSession session) {
        //拼装请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", oAuth2Properties.getWeibo().getClientId());
        params.put("client_secret", oAuth2Properties.getWeibo().getClientSecret());
        params.put("grant_type", oAuth2Properties.getWeibo().getGrantType());
        params.put("redirect_uri", OAuth2Constant.WeiBo.REDIRECT_URI);

        try {
            //拿到微博的回调，换取Access_Token
            HttpResponse response = HttpRequest.post(OAuth2Constant.ACCESS_TOKEN).form(params).execute();
            if (response.isOk()) {
                SocialUserVo socialUser = JSONUtil.toBean(response.body(), SocialUserVo.class);
                //远程调用会员保存接口
                try {
                    R r = memberFeignClient.oauth2Login(socialUser);
                    MemberRespVo data = r.getFeignData(new TypeReference<MemberRespVo>() {
                    });
                    session.setAttribute(RedisConstant.Auth.SESSION_LOGIN_USER, data);
                    return "redirect:http://www.gulimall.com";
                } catch (Exception e) {
                    log.error("远程调用 memberFeignClient.oauth2Login 失败", e);
                    return "redirect:http://auth.gulimall.com/login.html";
                }
            }
            return "redirect:http://auth.gulimall.com/login.html";
        } catch (HttpException e) {
            log.error("远程调用 {} 失败", OAuth2Constant.ACCESS_TOKEN);
            e.printStackTrace();
            return "redirect:http://auth.gulimall.com/login.html";
        }
    }
}
