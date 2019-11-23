package com.daliy.foodie.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Description Cors 跨域配置
 * Created by perl on 11/23/19.
 */
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
        config.addAllowedOrigin("*");
        /* 允许的header */
        config.addAllowedHeader("*");
        /* 允许的请求类型 */
        config.addAllowedMethod("*");

        /* 添加url映射路径 */
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", config);

        return new CorsFilter(corsSource);
    }
}
