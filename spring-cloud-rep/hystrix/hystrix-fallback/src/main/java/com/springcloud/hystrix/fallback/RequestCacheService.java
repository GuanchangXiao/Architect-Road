package com.springcloud.hystrix.fallback;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.springcloud.feign.client.intf.Friend;
import com.springcloud.feign.client.intf.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by perl on 2020-03-15.
 */
@Slf4j
@Service
public class RequestCacheService {

    @Autowired
    private MyService iService;

    @CacheResult
    @HystrixCommand(commandKey = "cacheKey")
    public Friend requestCache(@CacheKey String name) {
        Friend friend = new Friend();
        friend.setName(name);
        log.info("reuqest cache : " + friend);
        return iService.hello(friend);
    }
}
