package top.codecrab.gulimall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableRabbit
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "top.codecrab.gulimall.order.dao")
@ComponentScan({"top.codecrab.gulimall.order", "top.codecrab.common"})
public class GulimallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallOrderApplication.class, args);
    }

}
