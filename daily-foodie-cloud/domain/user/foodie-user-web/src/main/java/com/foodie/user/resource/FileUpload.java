package com.foodie.user.resource;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file")
@PropertySource("classpath:file-upload.properties")
@Getter
@Setter
public class FileUpload {
    private String imageUserFaceLocation;
    private String imageServerUrl;
}
