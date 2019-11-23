package com.daliy.foodie.api;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 *
 * @Description 将项目打成war包
 * Created by perl on 11/23/19.
 */
public class ApplicationBootstarpWarPacking extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 指向Application这个springboot启动类
        return builder.sources(ApplicationBootstrap.class);
    }
}
