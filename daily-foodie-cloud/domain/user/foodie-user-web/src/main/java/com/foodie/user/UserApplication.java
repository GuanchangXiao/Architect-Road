package com.foodie.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Created by perl on 2020-03-07.
 */
@SpringBootApplication
@MapperScan(basePackages = "com.foodie.user.mapper")
@ComponentScan(basePackages = {"com.foodie.user", "com.foodie.component" , "org.n3r.idworker"})
@EnableDiscoveryClient
// TODO Feign Annotation
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
