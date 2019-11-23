package com.daliy.foodie.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;


/**
 * Created by perl on 11/23/19.
 */
@SpringBootApplication
/* 扫描mapper */
@MapperScan(basePackages = "com.daliy.foodie.mapper")
/* 扫描组件 */
@ComponentScan(basePackages = { "com.daliy.foodie"})
//@ComponentScan(basePackages = { "com.daliy.foodie", "org.n3r.idworker"})
/* 开启定时任务 */
//@EnableScheduling
public class ApplicationBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationBootstrap.class,args);
    }
}
