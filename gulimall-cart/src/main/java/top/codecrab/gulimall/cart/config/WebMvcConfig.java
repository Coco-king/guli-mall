package top.codecrab.gulimall.cart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.codecrab.gulimall.cart.interceptor.CartInterceptor;

import javax.annotation.Resource;

/**
 * @author codecrab
 * @since 2021年06月24日 17:23
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private CartInterceptor cartInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cartInterceptor).addPathPatterns("/**");
    }

}
