package com.rabbit.component.core.producer.service.impl;

import com.rabbit.component.common.commons.MessageStatus;
import com.rabbit.component.core.producer.entity.BrokerMessage;
import com.rabbit.component.core.producer.mapper.BrokerMessageMapper;
import com.rabbit.component.core.producer.service.RabbitDBStoreService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by perl on 2020-02-22.
 */
public class RabbitDBStoreServiceImpl implements RabbitDBStoreService {

    @Autowired
    private BrokerMessageMapper messageMapper;

    @Override
    public int insert(BrokerMessage brokerMessage) {
        return messageMapper.insert(brokerMessage);
    }

    @Override
    public BrokerMessage selectByMessageId(String messageId) {
        return messageMapper.selectByPrimaryKey(messageId);
    }

    @Override
    public void success(String messageId) {
        messageMapper.changeBrokerMessageStatus(messageId, MessageStatus.SEND_OK.getCode(), new Date());
    }

    @Override
    public void failure(String messageId) {
        messageMapper.changeBrokerMessageStatus(messageId, MessageStatus.SEND_FAIL.getCode(), new Date());
    }

    @Override
    public List<BrokerMessage> fetchTimeOutMessage4Retry(MessageStatus messageStatus) {
        return messageMapper.queryBrokerMessageStatus4Timeout(messageStatus.getCode());
    }

    @Override
    public int updateTryCount(String brokerMessageId) {
        return messageMapper.update4TryCount(brokerMessageId, new Date());
    }
}
