package top.codecrab.gulimall.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"top.codecrab.gulimall.thirdparty", "top.codecrab.common.exception"})
public class GulimallThirdPartyApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallThirdPartyApplication.class, args);
    }

}
