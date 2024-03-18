package com.healthyrecipes.config;

import io.github.briqt.spark4j.SparkClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:86198
 * @DATE:2024/3/18 10:19
 * @DESCRIPTION:  星火配置
 * @VERSION:1.0
 */
@Configuration
@ConfigurationProperties(prefix = "xunfei.client")
@Data
public class XingHuoConfiguration {
    private String appid;
    private String apiSecret;
    private String apiKey;

    @Bean
    public SparkClient sparkClient() {
        SparkClient sparkClient = new SparkClient();
        sparkClient.apiKey = apiKey;
        sparkClient.apiSecret = apiSecret;
        sparkClient.appid = appid;
        return sparkClient;
    }
}

