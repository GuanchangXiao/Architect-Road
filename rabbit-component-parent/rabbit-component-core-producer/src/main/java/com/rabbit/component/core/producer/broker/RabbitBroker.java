package com.rabbit.component.core.producer.broker;

import com.rabbit.component.api.message.Message;

/**
 * RabbitBroker 具体发送消息的接口抽象
 * Created by perl on 2020-02-19.
 */
public interface RabbitBroker {

    /**
     * 发送RAPID消息
     * @param message
     */
    void rapidSend(Message message);

    /**
     * 发送CONFIRM消息
     * @param message
     */
    void confirmSend(Message message);

    /**
     * 发送RELIANT消息
     * @param message
     */
    void reliantSend(Message message);

    /**
     * 发送批量消息
     */
    void batchSend();
}
