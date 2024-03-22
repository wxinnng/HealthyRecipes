package com.healthyrecipes.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogComment implements Serializable {
    // 饮食圈评论
    Integer id;
    Integer userId;
    Integer logId;
    String content;
    Date createTime;
    Date updateTime;
    Integer parentCommentId;
    Integer status;
    Integer likes;
}