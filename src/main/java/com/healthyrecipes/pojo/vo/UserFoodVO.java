package com.healthyrecipes.pojo.vo;

import com.healthyrecipes.pojo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:86198
 * @DATE:2024/3/6 9:25
 * @DESCRIPTION: User + FoodVO
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFoodVO {
    private User user;
    private FoodVO target;
}
