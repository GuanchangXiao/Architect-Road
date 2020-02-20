package com.rabbit.component.common.serializer.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rabbit.component.common.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by perl on 2020-02-20.
 */
@Slf4j
public class JacksonSerializer implements Serializer {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.disable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        mapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
        mapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    private final JavaType type;

    private JacksonSerializer(JavaType type) {
        this.type = type;
    }

    public JacksonSerializer(Type type) {
        this.type = mapper.getTypeFactory().constructType(type);
    }

    public static Serializer createParametricType(Class<?> cls) {
        return new JacksonSerializer(mapper.getTypeFactory().constructType(cls));
    }

    @Override
    public byte[] serializeRaw(Object data) {
        try {
            return mapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            log.error("Serialize Failed: {}", e);
        }
        return null;
    }

    @Override
    public String serialize(Object data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("Serialize Failed: {}", e);
        }
        return null;
    }

    @Override
    public <T> T deserialize(String str) {
        try {
            return mapper.readValue(str, type);
        } catch (IOException e) {
            log.error("Deserialize Failed: {}", e);
        }
        return null;
    }

    @Override
    public <T> T deserialize(byte[] bytes) {
        try {
            return mapper.readValue(bytes, type);
        } catch (IOException e) {
            log.error("Deserialize Failed: {}", e);
        }
        return null;
    }
}
