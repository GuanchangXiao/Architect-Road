package com.springcloud.feign.consumer.advanced;

import com.springcloud.feign.client.intf.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by perl on 2020-03-14.
 */
@RestController
@Slf4j
public class Controller{

    @Autowired
    private IService iService;


    @GetMapping("hello")
    public String hello() {
        return iService.hello();
    }

    @GetMapping("retry")
    public String retry(@RequestParam("timeout") int timeout) {
        return iService.retry(timeout);
    }

}
