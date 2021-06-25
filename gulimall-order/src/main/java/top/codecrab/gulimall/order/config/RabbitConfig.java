package top.codecrab.gulimall.order.config;

import cn.hutool.core.util.StrUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author codecrab
 * @since 2021年06月25日 16:14
 */
@Configuration
public class RabbitConfig {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @PostConstruct
    public void initRabbitTemplate() {
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             *  发送消息到达Broker时的回调
             * @param correlationData 消息的唯一ID
             * @param ack 是否成功
             * @param cause 异常信息
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println(StrUtil.format("ConfirmCallback：{}，是否成功到达：{}，异常信息：{}", correlationData, ack, cause));
            }
        });

        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             * 消息投递失败到管道失败的回调
             * @param message 投递失败的消息详情
             * @param replyCode 回复的状态码
             * @param replyText 回复的详情
             * @param exchange 当时消息所在的交换机
             * @param routingKey 当时消息所在的队列
             */
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println(StrUtil.format("ReturnCallback：{}，状态码：{}，详情：{}，交换机：{}，队列：{}", message, replyCode, replyText, exchange, routingKey));
            }
        });
    }
}
