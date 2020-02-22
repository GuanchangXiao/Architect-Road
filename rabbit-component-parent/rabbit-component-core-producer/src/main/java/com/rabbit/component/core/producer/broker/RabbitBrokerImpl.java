package com.rabbit.component.core.producer.broker;

import com.rabbit.component.api.message.Message;
import com.rabbit.component.api.message.MessageType;
import com.rabbit.component.common.commons.Consts;
import com.rabbit.component.common.commons.MessageStatus;
import com.rabbit.component.core.producer.entity.BrokerMessage;
import com.rabbit.component.core.producer.service.MessageDBStoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by perl on 2020-02-19.
 */
@Component
@Slf4j
public class RabbitBrokerImpl implements RabbitBroker {

    @Autowired
    private RabbitTemplatePool rabbitTemplatePool;

    @Autowired
    private MessageDBStoreService messageDBStoreService;

    @Override
    public void rapidSend(Message message) {
        message.setMessageType(MessageType.RAPID);
        sendKernel(message);
    }

    @Override
    public void confirmSend(Message message) {
        message.setMessageType(MessageType.CONFIRM);
        sendKernel(message);
    }

    @Override
    public void reliantSend(Message message) {
        message.setMessageType(MessageType.RELIANT);
        BrokerMessage brokerMessage = messageDBStoreService.selectByMessageId(message.getMessageId());
        if (brokerMessage == null) {
            // 如果没有获取到消息记录 则说明是一条新消息需要入库
            storeMessage2DB(message);
        }
        sendKernel(message);
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
        RabbitTemplate rabbitTemplate = rabbitTemplatePool.getTemplate(message);
        // 使用异步队列发送消息 提高消息发送吞吐量
        AsyncBaseQueue.submit(() -> rabbitTemplate.convertAndSend(topic, routingKey, message, correlationData));
//        rabbitTemplate.convertAndSend(topic, routingKey, message, correlationData);
        log.info("#RabbitBrokerImpl.sendKernel# Send Message : {}", messageId);
    }

    /**
     * 消息入库
     * @param message
     */
    private void storeMessage2DB(Message message) {
        // 设置消息内容
        BrokerMessage brokerMessage = new BrokerMessage();
        brokerMessage.setMessageId(message.getMessageId());
        brokerMessage.setMessage(message);
        brokerMessage.setStatus(MessageStatus.SEND_READY);
        // 设置时间
        Date now = new Date();
        brokerMessage.setCreateTime(now);
        brokerMessage.setUpdateTime(now);
        brokerMessage.setNextRetry(DateUtils.addMinutes(now, Consts.RETRY_TIME_OUT));
        // 入库
        messageDBStoreService.insert(brokerMessage);
    }
}
