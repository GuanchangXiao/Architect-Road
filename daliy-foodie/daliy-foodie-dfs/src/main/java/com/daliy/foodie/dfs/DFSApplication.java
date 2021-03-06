package com.daliy.foodie.dfs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Created by perl on 2020-02-09.
 */
@SpringBootApplication
@ComponentScan(basePackages = { "com.daliy.foodie","org.n3r.idworker"})
@MapperScan(basePackages = "com.daliy.foodie.mapper")
public class DFSApplication {

    public static void main(String[] args) {
        SpringApplication.run(DFSApplication.class, args);
    }
}
