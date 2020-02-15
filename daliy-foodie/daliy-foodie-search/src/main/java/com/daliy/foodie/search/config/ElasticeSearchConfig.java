package com.daliy.foodie.search.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Created by perl on 2020-01-12.
 */
@Configuration
public class ElasticeSearchConfig {

    /**
     * 解决启动时提示<>java.lang.IllegalStateException: availableProcessors is already set to [4], rejecting [4]</>的错误
     * https://github.com/netty/netty/issues/6956
     */
    @PostConstruct
    void init() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }
}
