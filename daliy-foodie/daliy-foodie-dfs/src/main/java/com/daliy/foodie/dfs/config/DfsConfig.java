package com.daliy.foodie.dfs.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by perl on 2020-02-14.
 */
@Component
@PropertySource("classpath:fastdfs.properties")
@ConfigurationProperties(prefix = "dfs")
@Getter
@Setter
public class DfsConfig {
    private String host;
}
