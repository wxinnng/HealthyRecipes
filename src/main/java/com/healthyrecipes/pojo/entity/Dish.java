package com.healthyrecipes.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:86198
 * @DATE:2024/3/1 10:42
 * @DESCRIPTION: Dish实体类
 * @VERSION:1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Dish {
    private Integer id; //id
    private String key;//关键字
    private String image;//图片链接
    private String name;//名字
    private Float score;//评分
    private String materials;//材料
    private String amount; //用量
    private String step;//步骤
    private String stepImg;//步骤图片
}
