package top.codecrab.gulimall.order;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import top.codecrab.gulimall.order.listener.User;

import javax.annotation.Resource;

@SpringBootTest
class GulimallOrderApplicationTests {

    private final String EXCHANGE_NAME = "TEST_EXCHANGE";
    private final String QUEUE_NAME = "TEST_QUEUE";

    @Resource
    private AmqpAdmin amqpAdmin;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    void createExchange() {
        Exchange exchange = new DirectExchange(EXCHANGE_NAME, true, false);
        amqpAdmin.declareExchange(exchange);
    }

    @Test
    void createQueue() {
        Queue queue = new Queue(QUEUE_NAME, true, false, false);
        amqpAdmin.declareQueue(queue);
    }

    @Test
    void createBinding() {
        Binding binding = new Binding(
                QUEUE_NAME,
                Binding.DestinationType.QUEUE,
                EXCHANGE_NAME,
                "hello.world",
                null
        );
        amqpAdmin.declareBinding(binding);
    }

    @Test
    void sendMessage() {
        User user = new User();
        user.setName("张三");
        user.setAge("20");

        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "hello.world", user);
    }
}
