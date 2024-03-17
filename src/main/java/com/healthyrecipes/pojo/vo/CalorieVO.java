package com.healthyrecipes.pojo.vo;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:86198
 * @DATE:2024/3/3 20:15
 * @DESCRIPTION:  添加每顿食物的VO
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalorieVO {
    private Integer id; // userId;
    private Integer type; // 类型0b 1l 2d
    private Integer operator;  // 0 - 1 +
    private Float g;           // 质量
    private Integer foodId;      // 食物id
    private Float cellulose;   //纤维素
    private Float calories;    //热量
    private Float fat;         //脂肪
    private Float carbohydrate;//碳水化合物
    private Float protein;     //蛋白质
}
