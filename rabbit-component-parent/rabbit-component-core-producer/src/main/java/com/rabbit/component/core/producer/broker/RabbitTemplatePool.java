package com.rabbit.component.core.producer.broker;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.rabbit.component.api.exception.MessageRuntimeException;
import com.rabbit.component.api.message.Message;
import com.rabbit.component.api.message.MessageType;
import com.rabbit.component.common.convert.GenericMessageConverter;
import com.rabbit.component.common.convert.RabbitMessageConverter;
import com.rabbit.component.common.serializer.Serializer;
import com.rabbit.component.common.serializer.SerializerFactory;
import com.rabbit.component.common.serializer.impl.JacksonSerializerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by perl on 2020-02-20.
 * @RabbitTemplate rabbitTemplate池
 */
@Component
@Slf4j
public class RabbitTemplatePool implements RabbitTemplate.ConfirmCallback {

    private Map<String, RabbitTemplate> RABBIT_TEMPLATE_MAP = Maps.newConcurrentMap();
    private Splitter splitter = Splitter.on("#");
    private SerializerFactory serializerFactory = JacksonSerializerFactory.INSTANCE;

    @Autowired
    private ConnectionFactory connectionFactory;

    public RabbitTemplate getTemplate(Message message) throws MessageRuntimeException {
        Preconditions.checkNotNull(message);
        String topic = message.getTopic();
        String routingKey = message.getRoutingKey();
        MessageType messageType = message.getMessageType();
        RabbitTemplate template = RABBIT_TEMPLATE_MAP.get(topic);

        if (template == null) {
            log.info("#RabbitTemplatePool.getTemplate#topic is not exists,create it : {}",topic);
            template = new RabbitTemplate(connectionFactory);
            template.setExchange(topic);
            template.setRoutingKey(routingKey);
            template.setRetryTemplate(new RetryTemplate());

            if (!messageType.equals(MessageType.RAPID)) {
                template.setConfirmCallback(this::confirm);
            }

            // 设置序列化装换器
            Serializer serializer = serializerFactory.create();
            GenericMessageConverter gmc = new GenericMessageConverter(serializer);
            RabbitMessageConverter converter = new RabbitMessageConverter(gmc);
            template.setMessageConverter(converter);

            RABBIT_TEMPLATE_MAP.putIfAbsent(topic, template);
        }

        return template;
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        List<String> dataList = splitter.splitToList((CharSequence) correlationData);
        String messageId = dataList.get(0);
        Long timestamp = Long.valueOf(dataList.get(1));

        if (ack) {
            log.info("#RabbitTemplatePool.confirm#Message is Success >> messageId : {}, sendTime : {}", messageId, timestamp);
        }else {
            log.error("#RabbitTemplatePool.confirm#Message is Failed >> messageId : {}, sendTime : {}", messageId, timestamp);
        }

    }
}
