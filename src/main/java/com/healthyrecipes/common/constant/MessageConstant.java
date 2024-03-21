package com.healthyrecipes.common.constant;

import io.swagger.models.auth.In;

import java.util.ArrayList;

/**
 * @Author:86198
 * @DATE:2023/12/30 14:50
 * @DESCRIPTION:
 * @VERSION:1.0
 */
public interface MessageConstant {

    Integer ENABLE = 1;       //status:1启用
    Integer DISABLE = 0;      //status:0禁用

    String BAIDU_TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token";   //获取token的url

    String BAIDU_AI_URL = "https://aip.baidubce.com/rest/2.0/image-classify/v2/dish"; //百度AI识物的url

/*    String APP_ID = "48298377";
    String AK = "4jl2mGGFLmkLeDP76eyrNYZr";
    String SK = "cO6T4ASjRtrmyuRaxhjWukqDhIFjpcVS";*/

    String DEFAULT_PASSWORD="1096de3cb1daca74d90e0013c71c9c36"; //HealthyRecipes123

    Float[] EXERCISE = new Float[]{1.2f,1.375f,1.55f,1.725f,1.9f};  //exercise factor

    Float[] FAT = new Float[]{1.2f,1.1f,1.0f,0.9f};           //fat factor

    Float[] TARGET = new Float[]{0.8f,0.9f,1.0f,1.1f,1.2f};   //target factor

    Float[] TROPHIC_FACTOR = new Float[]{0.42f,0.30f,0.28f};  //car protein cell

    String PRECONDITION = "你是一个营养健康师，帮助回答各类关于饮食健康的问题";

    String COMMENT_KEY = "comment";                  //评论
    String USER_COMMENT_KEY = "user_comment" ;            //用户评论key

    String REDIS_EMAIL_KEY = "email:";   //邮箱验证码在Redis中的key

    String REDIS_LOG_KEY = "log:"; //log:userid:      0:总   1:饮食记录   2:吃多了 (type: list)

    String REDIS_CLOCK_IN_KEY = "clock:";  // clock:G_id:U_id:  (type: bitmap)

    Integer INVITATION_CODE_LEN = 7; //邀请码的长度

    Integer GROUP_MEMBER_2 = 2;    //Group中的人数  可以为 2 5 10
    Integer GROUP_MEMBER_5 = 5;
    Integer GROUP_MEMBER_10  =10;
}
