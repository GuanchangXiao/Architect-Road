package com.rabbit.component.api.inteface;

/**
 * Created by perl on 2020-02-19.
 * $SendCallback
 * 消息回调接口
 */
public interface SendCallback {

    /**
     * 消息发送成功回调
     */
    void onSuccess();

    /**
     * 消息发送失败回调
     */
    void onFail();
}
