package com.healthyrecipes.pojo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author:86198
 * @DATE:2024/3/7 14:05
 * @DESCRIPTION:
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO  {
    private Integer id;
    private Integer parentId;
    private Integer userid;
    private String username;
    private String avatar;
    private String content;
    private LocalDateTime date;
    private Integer likeNum;
    private Boolean isLike;
}
