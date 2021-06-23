package top.codecrab.gulimall.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author codecrab
 * @since 2021年06月21日 9:32
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 配置视图解析器等同于 @GetMapping("/login.html")
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/reg.html").setViewName("reg");
    }
}
