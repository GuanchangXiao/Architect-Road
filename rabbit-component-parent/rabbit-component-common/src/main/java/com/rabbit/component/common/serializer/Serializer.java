package com.rabbit.component.common.serializer;

/**
 * Created by perl on 2020-02-20.
 * 自定义序列化接口
 */
public interface Serializer {

    /**
     * 序列化为byte数组
     * @param data
     * @return
     */
    byte[] serializeRaw(Object data);

    /**
     * 序列化为字符串
     * @param data
     * @return
     */
    String serialize(Object data);

    /**
     * 字符串反序列化
     * @param str
     * @param <T>
     * @return
     */
    <T> T deserialize(String str);

    /**
     * byte数组反序列化
     * @param bytes
     * @param <T>
     * @return
     */
    <T> T deserialize(byte[] bytes);
}
