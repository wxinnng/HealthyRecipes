package com.healthyrecipes.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author:86198
 * @DATE:2024/3/20 17:25
 * @DESCRIPTION:
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogUserVO implements Serializable {
    private Integer id;  //id不用插入，自动生成
    private Integer userid;
    private Integer topicId;
    private String content;
    private Integer likeNum;
    private Integer disLikeNum;
    private LocalDateTime date;
    private String images;
    private String username;
    private String avatar;
}
