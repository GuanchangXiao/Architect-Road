package com.rabbit.component.common.convert;

import com.google.common.base.Preconditions;
import com.rabbit.component.common.serializer.Serializer;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import java.lang.reflect.Type;

/**
 * Created by perl on 2020-02-20.
 * GenericMessageConverter 通用消息装换对象
 */
public class GenericMessageConverter implements MessageConverter {

    private Serializer serializer;

    public GenericMessageConverter(Serializer serializer) {
        Preconditions.checkNotNull(serializer);
        this.serializer = serializer;
    }

    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        byte[] bytes = this.serializer.serializeRaw(object);
        return new Message(bytes, messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        return this.serializer.deserialize(message.getBody());
    }
}
