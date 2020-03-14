package com.foodie.shopcart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by perl on 2020-03-10.
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.foodie.shopcart",
    "com.foodie.component",
    "org.n3r.idworker"
})
@EnableDiscoveryClient
public class ShopcartApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopcartApplication.class, args);
    }
}
