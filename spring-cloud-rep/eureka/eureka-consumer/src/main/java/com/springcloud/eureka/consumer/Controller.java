package com.springcloud.eureka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by perl on 2020-02-29.
 */
@RestController
@Slf4j
public class Controller {

    @Autowired
    private LoadBalancerClient client;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("test")
    public String test() throws Exception {
        ServiceInstance instance = client.choose("eureka-client");
        if (instance == null) {
            throw new Exception("No Instance");
        }
        String target = String.format("http://%s:%s/test", instance.getHost(), instance.getPort());
        log.info("Target Url is : {}", target);
        return restTemplate.getForObject(target, String.class) + "##### Consumer it ...";
    }
}
