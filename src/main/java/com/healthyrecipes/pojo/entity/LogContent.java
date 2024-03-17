package com.healthyrecipes.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author:86198
 * @DATE:2024/3/17 18:43
 * @DESCRIPTION: Log 实体类
 * @VERSION:1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogContent implements Serializable {
    private Integer id;
    private Integer userId;
    private Integer topicId;
    private String content;
    private Integer likeNum;
    private Integer disLikeNum;
    private LocalDateTime date;
    private String images;
}
