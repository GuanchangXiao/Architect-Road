package com.rabbit.component.core.producer.config;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by perl on 2020-02-21.
 */
@Configuration
@AutoConfigureAfter(RabbitProducerMyBatisConfiguration.class)
public class RabbitProducerMybatisMapperScanerConfig {

    @Bean(name="rabbitProducerMapperScannerConfigurer")
    public MapperScannerConfigurer rabbitProducerMapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("rabbitProducerSqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.rabbit.component.core.producer.mapper");
        return mapperScannerConfigurer;
    }
}
