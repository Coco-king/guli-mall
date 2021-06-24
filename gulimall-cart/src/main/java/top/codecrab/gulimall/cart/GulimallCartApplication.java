package top.codecrab.gulimall.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = "top.codecrab.gulimall.cart.client")
public class GulimallCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallCartApplication.class, args);
    }

}
