package com.healthyrecipes.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogCommentDTO  {
    private Integer likeNum;
    private Integer dislikeNum;
    private List<Comment> logComments;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Comment {

        private int userId;

        private String username;

        private String avatar;

        private String content;

        private Integer likes;
        // 当前用户是否点赞
        private boolean isLike;
    }
}
