package top.codecrab.gulimall.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author codecrab
 * @since 2021年06月22日 10:45
 */
@Data
@Component("oAuth2Properties")
@ConfigurationProperties(prefix = "gulimall.oauth2")
public class OAuth2Properties {

    private WeiBo weibo;

    private QQ qq;

    @Data
    public static class WeiBo {
        private String clientId;
        private String clientSecret;
        private String grantType;
    }

    @Data
    public static class QQ {
        private String clientId;
        private String clientSecret;
        private String grantType;
    }
}
