package com.healthyrecipes.pojo.query;

import lombok.Data;

@Data
public class LogCommentQuery {
    private Integer logId;
    private Integer parentCommentId;
}
