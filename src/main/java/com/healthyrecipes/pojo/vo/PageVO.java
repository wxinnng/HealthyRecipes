package com.healthyrecipes.pojo.vo;

import com.github.pagehelper.Page;
import com.healthyrecipes.pojo.entity.Food;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:86198
 * @DATE:2024/3/5 17:54
 * @DESCRIPTION:
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageVO {
    private Integer num;
    private Page<Food> foods;
}
