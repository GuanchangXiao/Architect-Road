package com.daliy.foodie.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;


/**
 * Created by perl on 11/23/19.
 */
@SpringBootApplication
@MapperScan(basePackages = "com.daliy.foodie.mapper")
@ComponentScan(basePackages = { "com.daliy.foodie"})
//@ComponentScan(basePackages = { "com.daliy.foodie", "org.n3r.idworker"})
//@EnableScheduling
public class ApplicationBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationBootstrap.class,args);
    }
}
