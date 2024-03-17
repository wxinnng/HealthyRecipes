package com.healthyrecipes.pojo.vo;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @Author:86198
 * @DATE:2024/3/2 10:19
 * @DESCRIPTION: 前端传来的信息
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntakeVO {
    private Integer userid;
    private Integer sex;
    private Float height;
    private Float weight;
    private LocalDate birth;
    private Integer exercise;
    private Integer target;
    private Integer fat;
    private Float gym;
}
