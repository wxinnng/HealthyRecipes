package com.healthyrecipes.pojo.entity;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:86198
 * @DATE:2024/3/2 18:17
 * @DESCRIPTION: Food Dish DTO
 *@VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodDishDTO {
    private Integer foodNum;
    private Integer dishNum;
    private Integer category;
}
