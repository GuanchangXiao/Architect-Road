package com.springcloud.feign.client;

import com.springcloud.feign.client.intf.Friend;
import com.springcloud.feign.client.intf.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by perl on 2020-03-14.
 */
@RestController
@Slf4j
public class FeignController implements  IService{

    @Value("${server.port}")
    private String port;

    @Override
    public String hello() {
        return "This is " + port;
    }

    @Override
    public Friend hello(@RequestBody Friend friend) {
        log.info(friend.toString());
        friend.setPort(port);
        return friend;
    }

    @Override
    public String retry(@RequestParam("timeout") int timeout) {
        try {
            Thread.sleep(timeout * 1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        log.info("retry : {}",port);
        return "retry : " + port;
    }
}
