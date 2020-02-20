package com.rabbit.component.common.convert;

import com.google.common.base.Preconditions;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * Created by perl on 2020-02-20.
 * RabbitMessageConverter 使用装饰者模式实现
 */
public class RabbitMessageConverter implements MessageConverter {

    private GenericMessageConverter converter;

    public RabbitMessageConverter(GenericMessageConverter converter) {
        Preconditions.checkNotNull(converter);
        this.converter = converter;
    }

    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        return this.converter.toMessage(object, messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        return (com.rabbit.component.api.message.Message)this.converter.fromMessage(message);
    }
}
