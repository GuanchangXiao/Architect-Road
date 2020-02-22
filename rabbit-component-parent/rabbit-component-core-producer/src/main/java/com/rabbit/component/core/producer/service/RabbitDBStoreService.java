package com.rabbit.component.core.producer.service;

import com.rabbit.component.common.commons.MessageStatus;
import com.rabbit.component.core.producer.entity.BrokerMessage;

import java.util.List;

/**
 * Created by perl on 2020-02-22.
 */
public interface RabbitDBStoreService {

    /**
     * 插入一条新的Message
     * @param brokerMessage
     * @return
     */
    int insert(BrokerMessage brokerMessage);

    /**
     * 根据MessageId 查询消息
     * @param messageId
     * @return
     */
    BrokerMessage selectByMessageId(String messageId);

    /**
     * 保存发送成功的消息
     * @param messageId
     */
    void success(String messageId);

    /**
     * 保存发送失败的消息
     * @param messageId
     */
    void failure(String messageId);

    /**
     * 获取超时的消息并重新发送
     * @param messageStatus
     * @return
     */
    List<BrokerMessage> fetchTimeOutMessage4Retry(MessageStatus messageStatus);

    /**
     * 消息更新重试次数
     * @param brokerMessageId
     * @return
     */
    int updateTryCount(String brokerMessageId);

}
