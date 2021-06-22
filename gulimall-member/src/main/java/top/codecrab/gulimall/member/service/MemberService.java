package top.codecrab.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.gulimall.member.entity.MemberEntity;
import top.codecrab.gulimall.member.exception.PhoneExistException;
import top.codecrab.gulimall.member.exception.UsernameExistException;
import top.codecrab.gulimall.member.vo.MemberLoginVo;
import top.codecrab.gulimall.member.vo.MemberRegisterVo;
import top.codecrab.gulimall.member.vo.SocialUserVo;

import java.util.Map;

/**
 * 会员
 *
 * @author codecrab
 * @date 2021-05-28 22:40:42
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void register(MemberRegisterVo vo);

    void checkUsernameIsExist(String username) throws UsernameExistException;

    void checkPhoneIsExist(String phone) throws PhoneExistException;

    MemberEntity login(MemberLoginVo vo);

    MemberEntity login(SocialUserVo socialUser);
}

