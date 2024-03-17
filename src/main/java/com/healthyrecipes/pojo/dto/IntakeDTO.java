package com.healthyrecipes.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:86198
 * @DATE:2024/3/5 10:55
 * @DESCRIPTION:
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntakeDTO {
    private Integer id;
    private Integer userid;
    private Float cellulose;   //纤维素
    private Float calories;    //热量
    private Float fat;         //脂肪
    private Float carbohydrate;//碳水化合物
    private Float protein;     //蛋白质
}
