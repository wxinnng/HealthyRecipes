package com.healthyrecipes.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:86198
 * @DATE:2024/3/1 10:45
 * @DESCRIPTION: 收藏实体类
 * @VERSION:1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Save {
    private Integer id;
    private Integer userid;
    private Integer type;
    private Integer foodId;
}
