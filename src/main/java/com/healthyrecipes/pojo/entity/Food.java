package com.healthyrecipes.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:86198
 * @DATE:2024/2/18 13:21
 * @DESCRIPTION: 食品实体类
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Food {
    private Integer id;
    private Integer categoryId;
    private String title;
    private String image;
    private Float cellulose;   //纤维素
    private Float calories;    //热量
    private Float fat;         //脂肪
    private Float carbohydrate;//碳水化合物
    private Float protein;     //蛋白质
}
