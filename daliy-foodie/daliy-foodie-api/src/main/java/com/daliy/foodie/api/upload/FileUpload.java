package com.daliy.foodie.api.upload;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Description 读取file-upload-prod.properties的属性
 * Created by perl on 11/23/19.
 */

@Component
@ConfigurationProperties(prefix = "file")
@PropertySource("classpath:file-upload-prod.properties")
@Getter
@Setter
public class FileUpload {

    private String imageUserFaceLocation;
    private String imageServerUrl;
}
