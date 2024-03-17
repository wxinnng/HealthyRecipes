package com.healthyrecipes.pojo.vo;

import com.healthyrecipes.pojo.entity.Dish;
import com.healthyrecipes.pojo.entity.Food;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author:86198
 * @DATE:2024/3/1 10:41
 * @DESCRIPTION: 收藏的实体类
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveVO {
    private Integer userid;
    private List<Food> foods;
    private List<Dish> dishes;
}
