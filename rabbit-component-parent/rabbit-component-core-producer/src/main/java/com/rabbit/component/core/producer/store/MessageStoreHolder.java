package com.rabbit.component.core.producer.store;

import com.rabbit.component.api.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by perl on 2020-02-22.
 * MessageStoreHolder 批量发送消息时使用的缓存
 */
public class MessageStoreHolder {

    private List<Message> messageList = new ArrayList<>();

    private static final ThreadLocal<MessageStoreHolder> threadLocal = ThreadLocal.withInitial(() -> new MessageStoreHolder());

    public static void addMessage(Message message) {
        threadLocal.get().messageList.add(message);
    }

    public static List<Message> getAll() {
        List<Message> allMessage = threadLocal.get().messageList;
        threadLocal.remove();
        return allMessage;
    }
}
