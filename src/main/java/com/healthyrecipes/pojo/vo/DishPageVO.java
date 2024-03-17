package com.healthyrecipes.pojo.vo;

import com.github.pagehelper.Page;
import com.healthyrecipes.pojo.entity.Dish;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:86198
 * @DATE:2024/3/8 17:50
 * @DESCRIPTION: Dish分页查询结果的VO
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishPageVO {
    private Integer num;
    private Page<Dish> dishes;
}
