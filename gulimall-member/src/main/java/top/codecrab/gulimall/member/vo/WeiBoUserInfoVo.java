package top.codecrab.gulimall.member.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author codecrab
 * @since 2021年06月22日 12:00
 */
@Data
public class WeiBoUserInfoVo {

    private Long id;

    @JsonProperty("screen_name")
    private String screenName;

    private String name;

    private String location;

    private String domain;

    private String gender;

    private String remark;

    @JsonProperty("avatar_large")
    private String avatarLarge;
}
