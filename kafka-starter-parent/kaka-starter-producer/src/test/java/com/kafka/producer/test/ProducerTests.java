package com.kafka.producer.test;

import com.kafka.producer.service.ProducerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

/**
 * Created by perl on 2020-02-23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProducerTests {

    @Autowired
    private ProducerService producerService;

    @Test
    public void testSendMessage() throws InterruptedException {
        String topic = "topic01";

        for (int i = 0;i < 10;i++) {
            producerService.sendMessage(topic, "test message " + new Random().nextInt());
            Thread.sleep(1000);
        }

        Thread.sleep(Integer.MAX_VALUE);
    }
}
