package com.kafka.consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

/**
 * Created by perl on 2020-02-23.
 */
@Service
@Slf4j
public class ConsumerService {

    /**
     * 消费消息
     * @param record
     * @param ack
     * @param consumer
     */
    @KafkaListener(groupId = "group01", topics = "topic01")
    public void onMessage(ConsumerRecord<String, Object> record, Acknowledgment ack, Consumer<?,?> consumer) {
        log.info("consumer on message : {}",record.value());
        // 手动签收消息
        ack.acknowledge();
    }
}
