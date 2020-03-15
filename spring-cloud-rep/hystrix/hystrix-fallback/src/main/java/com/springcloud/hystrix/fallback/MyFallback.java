package com.springcloud.hystrix.fallback;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.springcloud.feign.client.intf.Friend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by perl on 2020-03-15.
 */
@Slf4j
@Component
public class MyFallback implements MyService{
    @Override
    public String hello() {
        return null;
    }

    @Override
    public Friend hello(Friend friend) {
        return null;
    }

    @Override
    public String retry(int timeout) {
        return "Time Out ...";
    }

    @Override
    @HystrixCommand(fallbackMethod = "error2")
    public String error() {
        log.info("Fallback One...");
        throw new RuntimeException("Fallback One...");
    }
    @HystrixCommand(fallbackMethod = "error3")
    public String error2() {
        log.info("Fallback Two...");
        throw new RuntimeException("Fallback Two...");
    }

    public String error3() {
        log.info("Fallback Three...");
        return "done";
    }
}
