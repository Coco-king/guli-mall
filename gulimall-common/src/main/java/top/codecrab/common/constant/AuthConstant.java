package top.codecrab.common.constant;

/**
 * @author codecrab
 * @since 2021年06月24日 17:13
 */
public interface AuthConstant {
    String CODE = RedisConstant.PREFIX + "sms:code:";
    String SESSION_LOGIN_USER = "loginUser";
    String TEMP_USER_COOKIE_KEY = "user-key";
    Integer TEMP_USER_COOKIE_TIMEOUT = 60 * 60 * 24 * 30;
}
