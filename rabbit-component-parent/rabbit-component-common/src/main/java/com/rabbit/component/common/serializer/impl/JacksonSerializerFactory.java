package com.rabbit.component.common.serializer.impl;

import com.rabbit.component.api.message.Message;
import com.rabbit.component.common.serializer.Serializer;
import com.rabbit.component.common.serializer.SerializerFactory;

/**
 * Created by perl on 2020-02-20.
 */
public class JacksonSerializerFactory implements SerializerFactory {

    public static final SerializerFactory INSTANCE = new JacksonSerializerFactory();

    @Override
    public Serializer create() {
        return JacksonSerializer.createParametricType(Message.class);
    }
}
