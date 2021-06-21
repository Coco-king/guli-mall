package top.codecrab.gulimall.member.exception;

/**
 * @author codecrab
 * @since 2021年06月21日 17:59
 */
public class PhoneExistException extends RuntimeException {

    public PhoneExistException() {
        super("手机号已经注册");
    }

}
