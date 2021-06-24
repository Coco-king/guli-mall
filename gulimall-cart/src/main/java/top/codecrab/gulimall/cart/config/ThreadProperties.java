package top.codecrab.gulimall.cart.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author codecrab
 * @since 2021年06月20日 20:17
 */
@Data
@Component
@ConfigurationProperties(prefix = "gulimall.thread")
public class ThreadProperties {
    private Integer coreSize;
    private Integer maxSize;
    private Integer keepAliveTime;
}
