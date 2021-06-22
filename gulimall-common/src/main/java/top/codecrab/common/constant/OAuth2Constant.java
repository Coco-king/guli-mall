package top.codecrab.common.constant;

/**
 * @author codecrab
 * @since 2021年06月22日 10:45
 */
public interface OAuth2Constant {
    String ACCESS_TOKEN = "https://api.weibo.com/oauth2/access_token";

    interface WeiBo {
        String REDIRECT_URI = "http://auth.gulimall.com/oauth2.0/weibo/success";
        String USER_SHOW = "https://api.weibo.com/2/users/show.json";
    }

    interface QQ {
    }
}
