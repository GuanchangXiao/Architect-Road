package com.rabbit.component.core.producer.autoconfigure;

import com.rabbit.component.task.annotation.EnableElasticJob;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by perl on 2020-02-19.
 * $RabbitProducerAutoConfiguration
 * Springboot 自动装配
 */
@Configuration
@EnableElasticJob
@ComponentScan({"com.rabbit.component.core.producer.*"})
public class RabbitProducerAutoConfiguration {


}
