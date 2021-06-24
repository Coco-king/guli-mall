package top.codecrab.gulimall.cart.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import top.codecrab.common.constant.AuthConstant;
import top.codecrab.common.vo.MemberRespVo;
import top.codecrab.gulimall.cart.to.UserInfoTo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @author codecrab
 * @since 2021年06月24日 17:02
 */
@Component
public class CartInterceptor implements HandlerInterceptor {

    public static final ThreadLocal<UserInfoTo> THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        UserInfoTo userInfoTo = new UserInfoTo();

        MemberRespVo user = (MemberRespVo) session.getAttribute(AuthConstant.SESSION_LOGIN_USER);
        if (user != null) {
            //登录，配置用户id
            userInfoTo.setUserId(user.getId());
        }

        //为空，未登录，获取cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AuthConstant.TEMP_USER_COOKIE_KEY)) {
                    userInfoTo.setUserKey(cookie.getValue());
                    userInfoTo.setIsTempUser(true);
                }
            }
        }

        //如果cookie中没有指定的key
        if (!userInfoTo.getIsTempUser()) {
            userInfoTo.setUserKey(UUID.randomUUID().toString());
        }

        //存入当前线程中
        THREAD_LOCAL.set(userInfoTo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserInfoTo infoTo = THREAD_LOCAL.get();
        //如果不是在cookie中带来的 值，再保存到cookie
        if (!infoTo.getIsTempUser()) {
            Cookie cookie = new Cookie(AuthConstant.TEMP_USER_COOKIE_KEY, infoTo.getUserKey());
            cookie.setDomain("gulimall.com");
            cookie.setMaxAge(AuthConstant.TEMP_USER_COOKIE_TIMEOUT);
            response.addCookie(cookie);
        }
    }
}
