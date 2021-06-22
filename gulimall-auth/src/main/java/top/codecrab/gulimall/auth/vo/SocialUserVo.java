package top.codecrab.gulimall.auth.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author codecrab
 * @since 2021年06月22日 11:39
 */
@Data
public class SocialUserVo {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("uid")
    private Long uid;
}
