package com.healthyrecipes.pojo.entity;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * @Author:86198
 * @DATE:2024/2/29 8:51
 * @DESCRIPTION: 返回到管理端的统计的数据
 * @VERSION:1.0
 */
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class Data {
    private Long userOnline;
    private Long apiUse;
    private Long registerNum;
    private FoodDishDTO fd;
    private List<String> timeLine;
    private List<Integer> userLine;
}
