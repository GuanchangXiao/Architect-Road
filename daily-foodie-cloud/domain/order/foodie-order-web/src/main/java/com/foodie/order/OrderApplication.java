package com.foodie.order;

import com.foodie.item.service.ItemService;
import com.foodie.order.fallback.itemservice.ItemCommentsFeignClient;
import com.foodie.shopcart.service.ShopcartService;
import com.foodie.user.service.AddressService;
import com.foodie.user.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Created by perl on 2020-03-08.
 */
@SpringBootApplication
@MapperScan(basePackages = "com.foodie.order.mapper")
@ComponentScan(
    basePackages = {
        "com.foodie.order",
        "com.foodie.component",
        "org.n3r.idworker"
    }
)
@EnableDiscoveryClient
@EnableFeignClients(
//    basePackages = {
//        "com.foodie.user.service",
//        "com.foodie.item.service",
//        "com.foodie.shopcart.service",
//        "com.foodie.order.fallback.**"
//    }
//    解决Ambiguous mapping错误
    clients = {
        ItemCommentsFeignClient.class,
        ItemService.class,
        UserService.class,
        AddressService.class,
        ShopcartService.class
    }
)
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
