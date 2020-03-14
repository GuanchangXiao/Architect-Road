package com.springcloud.ribbon.configuration;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.springcloud.ribbon.rules.MyRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by perl on 2020-03-14.
 */
@Configuration
// 配置全局LB策略 方式三  注意注解配置和yml文件配置同时存在 注解优先级更高
//@RibbonClient(name = "eureka-client", configuration = com.netflix.loadbalancer.RandomRule.class)
public class RibbonConfiguration {

    /**
     * 配置全局LB策略 方式一
     * @return
     */
    @Bean
    public IRule defaultLBS() {
//        return new RandomRule();
        return new MyRule();
    }
}
