package com.rabbit.component.api.inteface;

import com.rabbit.component.api.message.Message;

/**
 * Created by perl on 2020-02-19.
 * $MessageListener
 * 消息监听类接口
 */
public interface MessageListener {

    void onMessage(Message message);
}
