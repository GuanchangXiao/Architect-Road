package com.springcloud.feign.consumer.controller;

import com.springcloud.feign.consumer.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by perl on 2020-03-14.
 */
@RestController
public class MyController {

    @Autowired
    private MyService myService;

    @GetMapping("hello")
    public String hello() {
        return myService.hello();
    }
}
