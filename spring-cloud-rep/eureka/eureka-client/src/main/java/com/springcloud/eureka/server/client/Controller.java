package com.springcloud.eureka.server.client;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by perl on 2020-02-28.
 */
@RestController
public class Controller {

    @RequestMapping("test")
    public String test() {
        return "test";
    }
}
