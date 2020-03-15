package com.springcloud.hystrix.fallback;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.springcloud.feign.client.intf.Friend;
import lombok.Cleanup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by perl on 2020-03-15.
 */
@RestController
public class HystrixController {

    @Autowired
    private MyService myService;

    @Autowired
    private RequestCacheService cacheService;

    @GetMapping("fallback")
    public String fallback() {
        return myService.error();
    }

    @GetMapping("timeout")
//    @HystrixCommand(
//            fallbackMethod = "timeoutFallback",
//            commandProperties = {
//                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value="3000")
//            }
//    ) // 注解配置方法级别的超时属性
    public String timeout(int timeout) {
        return myService.retry(timeout);
    }

    public String timeoutFallback(int timeout) {
        return  "OK";
    }

    @GetMapping("cache")
    public Friend cache(String name) {
        @Cleanup
        HystrixRequestContext ctx = HystrixRequestContext.initializeContext();

        Friend friend = cacheService.requestCache(name);
        friend = cacheService.requestCache(name);
        return friend;
    }
}
