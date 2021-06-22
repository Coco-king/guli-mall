package top.codecrab.gulimall.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @author codecrab
 * @since 2021年06月22日 18:37
 */
@Configuration
@EnableRedisHttpSession
public class SessionConfig {

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer(Jackson2JsonRedisSerializer<Object> valueSerializer) {
        return valueSerializer;
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("GULISESSIONID");
        serializer.setCookiePath("/");
        serializer.setDomainName("gulimall.com");
        return serializer;
    }

}
