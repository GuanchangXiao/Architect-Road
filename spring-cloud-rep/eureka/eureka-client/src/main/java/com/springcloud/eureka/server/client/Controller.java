package com.springcloud.eureka.server.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by perl on 2020-02-28.
 */
@RestController
public class Controller {

    @Value("${server.port}")
    private String port;

    @RequestMapping("hello")
    public String hello() {
        return "hello : " + port;
    }
}
