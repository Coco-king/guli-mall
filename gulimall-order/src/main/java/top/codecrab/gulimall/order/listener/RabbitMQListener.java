package top.codecrab.gulimall.order.listener;

import cn.hutool.core.util.StrUtil;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author codecrab
 * @since 2021年06月25日 17:09
 */
@Component
@RabbitListener(queues = {"TEST_QUEUE"})
public class RabbitMQListener {

    @RabbitHandler
    public void rabbitHandler(Message message, User user, Channel channel) {
        System.out.println(StrUtil.format("Message：{}，User：{}，Channel：{}", message, user, channel));
        //按顺序自增的
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.out.println(deliveryTag);
        //签收，手动ack，multiple=false，非批量模式，一个一个接
        try {
            channel.basicAck(deliveryTag, false);
            //退货 deliveryTag：队列标签, multiple：是否批量, requeue：是否重新入队，false就把消息丢弃，true重新入队，可以再次被消费
            //channel.basicNack(deliveryTag, false, true);
            //拒收
            //channel.basicReject(deliveryTag, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RabbitHandler
    public void rabbitHandler(User2 user) {
        System.out.println(user);
    }

}
