package com.foodie.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Created by perl on 2020-03-08.
 */
@SpringBootApplication
@MapperScan(basePackages = "com.foodie.order.mapper")
@ComponentScan(basePackages = {"com.foodie.order", "com.foodie.component", "org.n3r.idworker"})
@EnableDiscoveryClient
// TODO Feign Annotation
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
