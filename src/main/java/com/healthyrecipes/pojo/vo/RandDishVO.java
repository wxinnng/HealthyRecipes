package com.healthyrecipes.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:86198
 * @DATE:2024/3/10 15:04
 * @DESCRIPTION:
 * @VERSION:1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RandDishVO {
    private Integer id;
    private String name;
    private String image;
}
