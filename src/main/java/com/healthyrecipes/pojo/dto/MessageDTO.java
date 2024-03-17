package com.healthyrecipes.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:86198
 * @DATE:2024/3/5 13:29
 * @DESCRIPTION:返回添加食物的热量信息
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO{
    private Integer id;
    private String title;
    private Float g;
    private String image;
}
