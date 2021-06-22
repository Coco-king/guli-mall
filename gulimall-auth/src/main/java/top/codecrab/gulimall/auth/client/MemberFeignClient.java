package top.codecrab.gulimall.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.codecrab.common.response.R;
import top.codecrab.gulimall.auth.vo.LoginVo;
import top.codecrab.gulimall.auth.vo.RegisterVo;
import top.codecrab.gulimall.auth.vo.SocialUserVo;

/**
 * @author codecrab
 * @since 2021年06月21日 18:38
 */
@FeignClient("gulimall-member")
public interface MemberFeignClient {

    @PostMapping("/member/member/register")
    R register(@RequestBody RegisterVo vo);

    @PostMapping("/member/member/login")
    R login(@RequestBody LoginVo vo);

    @PostMapping("/member/member/oauth2/login")
    R oauth2Login(@RequestBody SocialUserVo socialUser);
}
