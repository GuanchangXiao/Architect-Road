package com.rabbit.component.api.message;

import com.rabbit.component.api.exception.MessageRuntimeException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by perl on 2020-02-19.
 */
public class MessageBuilder {
    private String messageId;
    private String topic;
    private String routingKey;
    private Map<String, Object> attributes;
    private int delay;
    private MessageType messageType;

    private MessageBuilder() {

    }

    public static MessageBuilder getInstance() {
        return new MessageBuilder();
    }

    public MessageBuilder withMessageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public MessageBuilder withTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public MessageBuilder withRoutingKey(String routingKey) {
        this.routingKey = routingKey;
        return this;
    }

    public MessageBuilder withAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
        return this;
    }

    public MessageBuilder withAttribute(String key, Object value) {
        Map<String, Object> attributes = this.attributes;
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put(key, value);
        this.attributes = attributes;
        return this;
    }

    public MessageBuilder withDelay(int delay) {
        this.delay = delay;
        return this;
    }

    public MessageBuilder withMessageType(MessageType messageType) {
        this.messageType = messageType;
        return this;
    }

    public Message builder() {
        if (this.messageId == null) {
            this.messageId = UUID.randomUUID().toString();
        }
        if (this.topic == null) {
            throw new MessageRuntimeException("Message Topic can't be null");
        }
        Message message = new Message(this.messageId, this.topic, this.routingKey, this.attributes, this.delay, this.messageType);
        return message;
    }
}
