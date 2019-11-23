package com.daliy.foodie.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @Description Swagger2核心配置文件
 * ui老地址: http://localhost:8088/swagger-ui.html
 * ui新地址: http://localhost:8088/doc.html
 * Created by perl on 11/23/19.
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    /**
     * 配置swagger2 docket
     * @return
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.daliy.foodie.api.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("天天吃货 电商平台接口api")
                .contact(new Contact("daliy-foodie", "https://www.daliy-foodie.com", "abc@daliy-foodie.com"))
                .description("天天吃货api文档")
                .version("1.0.1")
                .termsOfServiceUrl("https://www.daliy-foodie.com")
                .build();
    }


}
