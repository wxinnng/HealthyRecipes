package com.healthyrecipes.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:86198
 * @DATE:2024/3/8 17:54
 * @DESCRIPTION: Dish DTO
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishDTO {
    private Integer id;
    private Integer pageNum;   //分页查询相关
    private Integer pageSize;  //分页查询相关
}
