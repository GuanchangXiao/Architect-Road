package com.rabbit.component.core.producer.broker;

import com.google.common.base.Preconditions;
import com.rabbit.component.api.exception.MessageException;
import com.rabbit.component.api.exception.MessageRuntimeException;
import com.rabbit.component.api.inteface.MessageProducer;
import com.rabbit.component.api.inteface.SendCallback;
import com.rabbit.component.api.message.Message;
import com.rabbit.component.api.message.MessageType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by perl on 2020-02-19.
 * ProducerClient 发送消息实现类
 */
@Component
public class ProducerClient implements MessageProducer {

    @Autowired
    private RabbitBroker rabbitBroker;

    @Override
    public void send(Message message) throws MessageRuntimeException {
        Preconditions.checkNotNull(message.getTopic());
        MessageType messageType = message.getMessageType();
        switch (messageType) {
            case RAPID:
                rabbitBroker.rapidSend(message);
                break;
            case CONFIRM:
                rabbitBroker.confirmSend(message);
                break;
            case RELIANT:
                rabbitBroker.reliantSend(message);
                break;
            default:
                throw new MessageRuntimeException("Message Type is not define");
        }
    }

    @Override
    public void send(List<Message> messageList) throws MessageRuntimeException {

    }

    @Override
    public void send(List<Message> messageList, SendCallback callback) throws MessageRuntimeException {

    }
}
