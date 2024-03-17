package com.healthyrecipes.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:86198
 * @DATE:2024/1/27 21:08
 * @DESCRIPTION: 百度AI识物返回结果的实体类中的result属性对应的实体类中的百科词条信息对应的实体类
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaiKeInfo {
    private String baike_info;
    private String image_url;
    private String description;
}
