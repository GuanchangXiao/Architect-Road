package com.rabbit.component.core.producer.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.rabbit.component.common.commons.Consts;
import com.rabbit.component.common.commons.MessageStatus;
import com.rabbit.component.core.producer.broker.RabbitBroker;
import com.rabbit.component.core.producer.entity.BrokerMessage;
import com.rabbit.component.core.producer.service.MessageDBStoreService;
import com.rabbit.component.task.annotation.ElasticJobConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by perl on 2020-02-22.
 */
@ElasticJobConfiguration(
        jobName = "com.rabbit.component.core.producer.task.RetryMessageDataflowJob",
        cron = "0/10 * * * * ?",
        description = "可靠性投递消息补偿机制Task",
        overwrite = true,
        shardingTotalCount = 1
)
@Component
@Slf4j
public class RetryMessageDataflowJob implements DataflowJob<BrokerMessage> {

    @Autowired
    private MessageDBStoreService messageDBStoreService;

    @Autowired
    private RabbitBroker rabbitBroker;

    @Override
    public List<BrokerMessage> fetchData(ShardingContext shardingContext) {
        return messageDBStoreService.fetchTimeOutMessage4Retry(MessageStatus.SEND_READY);
    }

    @Override
    public void processData(ShardingContext shardingContext, List<BrokerMessage> messageList) {
        messageList.stream()
                .forEach(message -> {
                    if (message.getTryCount() >= Consts.MAX_RETRY_COUNT) {
                        // 消息超过最大try count则默认失效
                        messageDBStoreService.failure(message.getMessageId());
                        log.warn("Message Already Time Out To Max >> MessageId : {}", message.getMessageId());
                    }else {
                        // 重发消息之前需要更新try count
                        messageDBStoreService.updateTryCount(message.getMessageId());
                        // 重新发送消息
                        rabbitBroker.reliantSend(message.getMessage());
                    }
                });
    }
}
