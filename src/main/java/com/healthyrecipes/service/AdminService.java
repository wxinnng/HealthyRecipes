package com.healthyrecipes.service;

import com.healthyrecipes.pojo.entity.FoodDishDTO;

/**
 * @Author:86198
 * @DATE:2024/3/2 18:19
 * @DESCRIPTION: Admin service
 * @VERSION:1.0
 */

public interface AdminService {
    /**
     * @description: 填充食物、菜品、菜品种类信息
     * @param: []
     * @return: com.healthyrecipes.pojo.entity.FoodDishDTO
     */
    FoodDishDTO fullData();
}
