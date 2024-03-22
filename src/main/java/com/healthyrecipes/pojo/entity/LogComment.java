package com.healthyrecipes.pojo.entity;

import lombok.Data;

import java.util.Date;

@Data
public class LogComment {
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