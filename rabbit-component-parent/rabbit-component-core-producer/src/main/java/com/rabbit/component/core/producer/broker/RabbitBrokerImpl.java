package com.rabbit.component.core.producer.broker;

import com.rabbit.component.api.message.Message;
import com.rabbit.component.api.message.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by perl on 2020-02-19.
 */
@Component
@Slf4j
public class RabbitBrokerImpl implements RabbitBroker {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void rapidSend(Message message) {
        message.setMessageType(MessageType.RAPID);
        sendKernel(message);
    }

    @Override
    public void confirmSend(Message message) {

    }

    @Override
    public void reliantSend(Message message) {

    }

    @Override
    public void batchSend(List<Message> messageList) {

    }

    /**
     * 发送消息核心方法 使用异步队列提高发送效率
     * @param message
     */
    private void sendKernel(Message message) {
        String topic = message.getTopic();
        String messageId = message.getMessageId();
        String routingKey = message.getRoutingKey();
        // 生产唯一id messageId + # + 当前时间戳
        String correlationString = String.format("%s#%s", messageId,System.currentTimeMillis());
        CorrelationData correlationData = new CorrelationData(correlationString);

        AsyncBaseQueue.submit(() -> rabbitTemplate.convertAndSend(topic, routingKey, message, correlationData));
//        rabbitTemplate.convertAndSend(topic, routingKey, message, correlationData);
        log.info("#RabbitBrokerImpl.sendKernel# Send Message : {}", messageId);
    }
}
