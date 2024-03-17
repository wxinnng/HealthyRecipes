package com.healthyrecipes.config;

import com.aliyun.broadscope.bailian.sdk.AccessTokenClient;
import com.aliyun.broadscope.bailian.sdk.ApplicationClient;
import com.aliyun.broadscope.bailian.sdk.models.ChatRequestMessage;
import com.aliyun.broadscope.bailian.sdk.models.ChatSystemMessage;
import com.aliyun.broadscope.bailian.sdk.models.ChatUserMessage;
import com.healthyrecipes.common.properties.BaiLianProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

/**
 * @Author:86198
 * @DATE:2024/3/8 21:15
 * @DESCRIPTION: 百炼
 * @VERSION:1.0
 */
@Configuration
@Slf4j
public class BaiLianConfiguration {

    @Autowired
    private BaiLianProperties baiLianProperties;

    @Bean
    public ApplicationClient getAConversationToBaiLian(){
        String ak = baiLianProperties.getAk();
        String sk = baiLianProperties.getSk();
        String app_id = baiLianProperties.getApp_id();
        String agent_id = baiLianProperties.getAgent_id();
        AccessTokenClient accessTokenClient = new AccessTokenClient(ak, sk, agent_id);

        String token = accessTokenClient.getToken();
        ApplicationClient client = ApplicationClient.builder().token(token).build();


        return client;
    }

}
