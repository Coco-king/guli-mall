package top.codecrab.gulimall.order.controller;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.codecrab.gulimall.order.listener.User;
import top.codecrab.gulimall.order.listener.User2;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author codecrab
 * @since 2021年06月25日 17:14
 */
@Controller
public class RabbitController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @ResponseBody
    @GetMapping("/sendMq")
    public String sendMq(@RequestParam(defaultValue = "10") Integer num) {
        for (int i = 0; i < num; i++) {
            if (i % 2 == 0) {
                User user = new User();
                user.setName("张三：" + i);
                user.setAge(num.toString());
                //CorrelationData：指定消息的唯一ID
                rabbitTemplate.convertAndSend("TEST_EXCHANGE", "hello.world", user, new CorrelationData(UUID.randomUUID().toString()));
            } else {
                User2 user = new User2();
                user.setUsername("张三：" + i);
                user.setSex(num.toString());
                rabbitTemplate.convertAndSend("TEST_EXCHANGE", "hello.world", user, new CorrelationData(UUID.randomUUID().toString()));
            }
        }
        return "ok";
    }

}
