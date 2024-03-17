package com.healthyrecipes.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:86198
 * @DATE:2024/1/27 20:25
 * @DESCRIPTION:
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaiduAIToken {
    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String scope;
    private String session_key;
    private String session_secret;
}
