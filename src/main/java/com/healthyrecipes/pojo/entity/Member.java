package com.healthyrecipes.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:86198
 * @DATE:2024/3/18 18:03
 * @DESCRIPTION: member entity
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    private Integer id;
    private Integer userId;
    private Integer groupId;
    private Integer isOwner;
}
