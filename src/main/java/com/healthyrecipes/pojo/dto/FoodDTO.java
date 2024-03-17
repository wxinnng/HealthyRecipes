package com.healthyrecipes.pojo.dto;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author:86198
 * @DATE:2024/2/19 18:44
 * @DESCRIPTION:
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodDTO implements Serializable {
    private Integer id;
    private Integer category_id;
    private String title;
    private Integer pageNum;   //分页查询相关
    private Integer pageSize;  //分页查询相关
    /*后续有对应的条件可以添加*/
}
