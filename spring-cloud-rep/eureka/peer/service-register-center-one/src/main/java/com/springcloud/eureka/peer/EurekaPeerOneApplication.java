package com.springcloud.eureka.peer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created by perl on 2020-02-29.
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaPeerOneApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaPeerOneApplication.class, args);
    }
}
