package com.healthyrecipes.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author:86198
 * @DATE:2024/3/7 13:09
 * @DESCRIPTION:  评论的实体类
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private Integer id;
    private Integer dishId;
    private Integer userId;
    private Integer parentId;
    private String content;
    private LocalDateTime date;
    private Integer likeNum;
}
