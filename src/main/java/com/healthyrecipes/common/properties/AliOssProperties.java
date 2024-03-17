package com.healthyrecipes.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author:86198
 * @DATE:2024/1/26 13:37
 * @DESCRIPTION:
 * @VERSION:1.0
 */
@Component
@ConfigurationProperties(prefix = "project.alioss")
@Data
public class AliOssProperties {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

}
