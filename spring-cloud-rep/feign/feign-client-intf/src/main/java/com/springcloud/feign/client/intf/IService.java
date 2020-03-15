package com.springcloud.feign.client.intf;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by perl on 2020-03-14.
 */
@FeignClient("feign-client")
public interface IService {

    @GetMapping("hello")
    String hello();

    @PostMapping("hello")
    Friend hello(@RequestBody Friend friend);

    @GetMapping("retry")
    String retry(@RequestParam("timeout") int timeout);

    @GetMapping("error")
    String error();
}
