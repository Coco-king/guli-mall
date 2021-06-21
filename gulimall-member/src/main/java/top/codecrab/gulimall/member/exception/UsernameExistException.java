package top.codecrab.gulimall.member.exception;

/**
 * @author codecrab
 * @since 2021年06月21日 17:59
 */
public class UsernameExistException extends RuntimeException {

    public UsernameExistException() {
        super("用户名已经存在");
    }

}
