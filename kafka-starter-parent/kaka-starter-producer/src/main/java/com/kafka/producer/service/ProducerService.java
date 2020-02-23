package com.kafka.producer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * Created by perl on 2020-02-23.
 */
@Service
@Slf4j
public class ProducerService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    /**
     * 发送消息
     * @param topic
     * @param data 消息内容
     */
    public void sendMessage(String topic, Object data) {
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, data);

        future.addCallback(successResult -> {
            log.info("send message to broker success : {}", successResult.toString());
        },failureException -> {
            log.error("send message to broker failure : {}", failureException.getMessage());
        });
    }
}
