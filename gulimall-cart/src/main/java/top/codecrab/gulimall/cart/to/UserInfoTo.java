package top.codecrab.gulimall.cart.to;

import lombok.Data;

/**
 * @author codecrab
 * @since 2021年06月24日 16:54
 */
@Data
public class UserInfoTo {
    private Long userId;
    private String userKey;
    private Boolean isTempUser = false;
}
