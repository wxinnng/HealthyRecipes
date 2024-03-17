package com.healthyrecipes.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:86198
 * @DATE:2024/1/27 21:05
 * @DESCRIPTION: 百度AI识物返回结果的实体类中的result属性对应的实体类
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private String name;
    private Float calorie;
    private Float probability;
    private BaiKeInfo baiKeInfo;
}
