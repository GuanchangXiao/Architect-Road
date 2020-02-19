package com.rabbit.component.api.inteface;

import com.rabbit.component.api.exception.MessageRuntimeException;
import com.rabbit.component.api.message.Message;

import java.util.List;

/**
 * Created by perl on 2020-02-19.
 * $MessageProducer
 * 消息生产者接口
 */
public interface MessageProducer {

    /**
     * 普通发送
     * @param message
     * @throws MessageRuntimeException
     */
    void send(Message message) throws MessageRuntimeException;

    /**
     * $send 消息批量发送
     * @param messageList
     * @throws MessageRuntimeException
     */
    void send(List<Message> messageList) throws MessageRuntimeException;

    /**
     * $send 消息异步发送 --> 回调 callback
     * @param messageList
     * @param callback
     * @throws MessageRuntimeException
     */
    void send(List<Message> messageList, SendCallback callback) throws MessageRuntimeException;
}
