package top.codecrab.gulimall.auth.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author codecrab
 * @since 2021年06月21日 17:18
 */
@NoArgsConstructor
@Data
public class RegisterVo {

    @NotBlank(message = "用户名不能为空")
    @Length(min = 4, max = 20, message = "用户名长度只能在4-20个字符之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度只能在6-20个字符之间")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    private String rePass;

    @NotBlank(message = "手机号码不能为空")
    @Pattern(regexp = "(?:0|86|\\+86)?1[3-9]\\d{9}", message = "手机号码格式有误")
    private String phone;

    @NotBlank(message = "验证码不能为空")
    private String code;
}
