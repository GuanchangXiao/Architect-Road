package com.rabbit.component.core.producer.broker;

import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.*;

/**
 * Created by perl on 2020-02-19.
 * AsyncBaseQueue 异步消息队列
 */
@Slf4j
public class AsyncBaseQueue {

    private static final int THREAD_SIZE = Runtime.getRuntime().availableProcessors();

    private static final int MAX_THREAD_SIZE = THREAD_SIZE * 2;

    private static final Long KEEP_ALIVE_TIME = 60L;

    private static final int QUEUE_SIZE = 10000;

    private static final ArrayBlockingQueue WORK_QUEUE = new ArrayBlockingQueue(QUEUE_SIZE);

    private static ExecutorService senderAsync = new ThreadPoolExecutor(
            THREAD_SIZE,
            MAX_THREAD_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            WORK_QUEUE,
            r -> {
                Thread thread = new Thread(r);
                thread.setName("rabbit_async_sender");
                return thread;
            },
            (r, executor) -> {
                log.error("Async Sender is Error Rejected!! >> Runnable : {}, Executor : {}", r, executor);
            }
    );

    public static void submit(Runnable r) {
         senderAsync.submit(r);
    }
}
