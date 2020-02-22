package com.rabbit.component.core.producer.entity;

import com.rabbit.component.api.message.Message;
import com.rabbit.component.common.commons.MessageStatus;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by perl on 2020-02-21.
 */
@Data
public class BrokerMessage implements Serializable {

    private static final long serialVersionUID = 7482144118682217536L;

    private String messageId;

    private Message message;

    private Integer tryCount = 0;

    private MessageStatus status;

    private Date nextRetry;

    private Date createTime;

    private Date updateTime;
}
