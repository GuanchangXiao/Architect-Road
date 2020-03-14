package com.springcloud.feign.consumer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by perl on 2020-03-14.
 */
@FeignClient("eureka-client")
public interface MyService {

    @GetMapping("hello")
    String hello();
}
