package top.codecrab.gulimall.member.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import top.codecrab.common.constant.OAuth2Constant;
import top.codecrab.common.utils.CommonUtils;
import top.codecrab.common.utils.PageUtils;
import top.codecrab.common.utils.Query;
import top.codecrab.gulimall.member.dao.MemberDao;
import top.codecrab.gulimall.member.dao.MemberLevelDao;
import top.codecrab.gulimall.member.entity.MemberEntity;
import top.codecrab.gulimall.member.entity.MemberLevelEntity;
import top.codecrab.gulimall.member.exception.PhoneExistException;
import top.codecrab.gulimall.member.exception.UsernameExistException;
import top.codecrab.gulimall.member.service.MemberService;
import top.codecrab.gulimall.member.vo.MemberLoginVo;
import top.codecrab.gulimall.member.vo.MemberRegisterVo;
import top.codecrab.gulimall.member.vo.SocialUserVo;
import top.codecrab.gulimall.member.vo.WeiBoUserInfoVo;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员
 *
 * @author codecrab
 * @date 2021-05-28 22:40:42
 */
@Slf4j
@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Resource
    private MemberLevelDao memberLevelDao;

    @Value("${gulimall.member.default-avatar}")
    private String defaultAvatar;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void register(MemberRegisterVo vo) {

        //校检手机号和用户名
        String username = vo.getUsername();
        this.checkUsernameIsExist(username);
        String phone = vo.getPhone();
        this.checkPhoneIsExist(phone);

        //查询默认等级
        List<MemberLevelEntity> levelEntities = memberLevelDao.selectList(new QueryWrapper<MemberLevelEntity>()
                .eq("default_status", 1));

        //封装数据
        MemberEntity member = new MemberEntity();
        member.setUsername(username);
        member.setMobile(phone);
        if (CollectionUtil.isNotEmpty(levelEntities)) {
            member.setLevelId(levelEntities.get(0).getId());
        }
        String encode = passwordEncoder.encode(vo.getPassword());
        member.setPassword(encode);
        member.setNickname(vo.getUsername());
        member.setHeader(defaultAvatar);
        member.setGender(0);
        member.setCity("地球");
        member.setSign(CommonUtils.getSign());
        member.setIntegration(0);
        member.setGrowth(0);
        member.setStatus(1);
        member.setCreateTime(LocalDateTime.now());
        baseMapper.insert(member);
    }

    @Override
    public void checkUsernameIsExist(String username) throws UsernameExistException {
        Integer count = baseMapper.selectCount(new QueryWrapper<MemberEntity>()
                .eq("username", username));
        if (count >= 1) {
            throw new UsernameExistException();
        }
    }

    @Override
    public void checkPhoneIsExist(String phone) throws PhoneExistException {
        Integer count = baseMapper.selectCount(new QueryWrapper<MemberEntity>()
                .eq("mobile", phone));
        if (count >= 1) {
            throw new PhoneExistException();
        }
    }

    @Override
    public MemberEntity login(MemberLoginVo vo) {
        MemberEntity one = baseMapper.selectOne(new QueryWrapper<MemberEntity>()
                .eq("username", vo.getLoginAccount())
                .or()
                .eq("mobile", vo.getLoginAccount())
        );

        if (one == null) {
            return null;
        }

        boolean matches = passwordEncoder.matches(vo.getPassword(), one.getPassword());
        return matches ? one : null;
    }

    @Override
    public MemberEntity login(SocialUserVo socialUser) {
        MemberEntity one = baseMapper.selectOne(new QueryWrapper<MemberEntity>()
                .eq("social_uid", socialUser.getUid()));

        Long expiresIn = socialUser.getExpiresIn();
        String accessToken = socialUser.getAccessToken();
        MemberEntity entity = new MemberEntity();
        //存在更新信息
        if (one != null) {
            //更新相关信息
            entity.setId(one.getId());
            entity.setExpiresIn(expiresIn);
            entity.setAccessToken(accessToken);
            baseMapper.updateById(one);

            one.setExpiresIn(expiresIn);
            one.setAccessToken(accessToken);
            return one;
        }

        //不存在新建用户
        //查询默认等级
        List<MemberLevelEntity> levelEntities = memberLevelDao.selectList(new QueryWrapper<MemberLevelEntity>()
                .eq("default_status", 1));
        if (CollectionUtil.isNotEmpty(levelEntities)) {
            entity.setLevelId(levelEntities.get(0).getId());
        }
        //封装请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("uid", socialUser.getUid());
        params.put("access_token", accessToken);
        try {
            //调用微博api查询用户信息信息
            HttpResponse response = HttpRequest.get(OAuth2Constant.WeiBo.USER_SHOW).form(params).execute();
            if (response.isOk()) {
                WeiBoUserInfoVo infoVo = JSONUtil.toBean(response.body(), WeiBoUserInfoVo.class);
                entity.setNickname(infoVo.getScreenName());
                entity.setHeader(infoVo.getAvatarLarge());
                entity.setGender("m".equals(infoVo.getGender()) ? 1 : 0);
                entity.setCity(infoVo.getLocation());
            }
        } catch (HttpException e) {
            log.error("远程调用 微博api {} 失败", OAuth2Constant.WeiBo.USER_SHOW);
            e.printStackTrace();
        }
        entity.setSign(CommonUtils.getSign());
        entity.setIntegration(0);
        entity.setGrowth(0);
        entity.setStatus(1);
        entity.setCreateTime(LocalDateTime.now());
        //设置微博返回的关键信息
        entity.setExpiresIn(expiresIn);
        entity.setAccessToken(accessToken);
        entity.setSocialUid(socialUser.getUid());
        baseMapper.insert(entity);
        return entity;
    }

}
