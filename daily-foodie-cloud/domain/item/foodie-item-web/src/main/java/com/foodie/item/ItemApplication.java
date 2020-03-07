package com.foodie.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Created by perl on 2020-02-29.
 */
@SpringBootApplication
@MapperScan(basePackages = "com.foodie.item.mapper")
@ComponentScan(basePackages = {"com.foodie.item", "org.n3r.idworker"})
@EnableDiscoveryClient
// TODO Feign Annotation
public class ItemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class, args);
    }
}
