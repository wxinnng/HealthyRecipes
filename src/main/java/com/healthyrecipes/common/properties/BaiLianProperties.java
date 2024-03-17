package com.healthyrecipes.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author:86198
 * @DATE:2024/3/8 21:05
 * @DESCRIPTION: 百炼配置文件
 * @VERSION:1.0
 */
@Component
@ConfigurationProperties(prefix = "project.bailian")
@Data
public class BaiLianProperties {
    private String ak;
    private String sk;
    private String app_id;
    private String agent_id;
}
