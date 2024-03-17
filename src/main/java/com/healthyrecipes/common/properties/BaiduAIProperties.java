package com.healthyrecipes.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Author:86198
 * @DATE:2024/1/31 13:57
 * @DESCRIPTION:
 * @VERSION:1.0
 */
@Component
@ConfigurationProperties(prefix = "project.baidu")
@Data
public class BaiduAIProperties {

    /*百度AI识物*/
    private String appId;
    private String ak;
    private String sk;
}
