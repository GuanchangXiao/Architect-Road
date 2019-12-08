package com.daliy.foodie.api.config;

import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Description Cors 跨域配置
 * Created by perl on 11/23/19.
 */
@Configuration
@NoArgsConstructor
public class CorsConfig {

    /**
     * JavaBean
     * 注意: CorsFilter是springfarmework包,不要导错包了
     * @return CorsFilter
     */
    @Bean
    public CorsFilter corsFilter () {
        CorsConfiguration config = new CorsConfiguration();

        /* 允许跨域 */
        config.addAllowedOrigin("http://localhost:8080");
//        config.addAllowedOrigin("http://shop.z.mukewang.com:8080");
//        config.addAllowedOrigin("http://center.z.mukewang.com:8080");
//        config.addAllowedOrigin("http://shop.z.mukewang.com");
//        config.addAllowedOrigin("http://center.z.mukewang.com");
//        config.addAllowedOrigin("*");

        /* 设置是否携带cookie信息 */
        config.setAllowCredentials(true);
        /* 允许的header */
        config.addAllowedHeader("*");
        /* 允许的请求类型 */
        config.addAllowedMethod("*");

        /* 为url添加映射路径 */
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", config);

        return new CorsFilter(corsSource);
    }
}
