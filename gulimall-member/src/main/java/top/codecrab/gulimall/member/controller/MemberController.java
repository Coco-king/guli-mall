package top.codecrab.gulimall.member.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.codecrab.common.response.ErrorCodeEnum;
import top.codecrab.common.response.R;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.gulimall.member.client.CouponFeignClient;
import top.codecrab.gulimall.member.entity.MemberEntity;
import top.codecrab.gulimall.member.exception.PhoneExistException;
import top.codecrab.gulimall.member.exception.UsernameExistException;
import top.codecrab.gulimall.member.service.MemberService;
import top.codecrab.gulimall.member.vo.MemberLoginVo;
import top.codecrab.gulimall.member.vo.MemberRegisterVo;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * 会员
 *
 * @author codecrab
 * @date 2021-05-28 22:40:42
 */
@Slf4j
@RestController
@RequestMapping("member/member")
public class MemberController {

    @Resource
    private MemberService memberService;

    @Resource
    private CouponFeignClient couponFeignClient;

    @GetMapping("/coupons")
    public R coupons() {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("张三");

        R coupons = couponFeignClient.memberCoupons();
        return R.ok().data("member", memberEntity).data("coupons", coupons.getFeignData());
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    @PostMapping("/register")
    public R register(@RequestBody MemberRegisterVo vo) {
        try {
            memberService.register(vo);
        } catch (PhoneExistException e) {
            log.error("注册用户异常", e);
            return R.error(ErrorCodeEnum.PHONE_IS_EXIST);
        } catch (UsernameExistException e) {
            log.error("注册用户异常", e);
            return R.error(ErrorCodeEnum.USERNAME_IS_EXIST);
        }
        return R.ok();
    }

    @PostMapping("/login")
    public R login(@RequestBody MemberLoginVo vo) {
        MemberEntity memberEntity = memberService.login(vo);
        if (memberEntity == null) {
            return R.error(ErrorCodeEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        return R.ok();
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody MemberEntity member) {
        memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody MemberEntity member) {
        memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
