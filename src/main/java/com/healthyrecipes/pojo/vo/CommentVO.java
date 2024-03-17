package com.healthyrecipes.pojo.vo;

import com.healthyrecipes.pojo.dto.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author:86198
 * @DATE:2024/3/7 13:40
 * @DESCRIPTION:  评论回复的VO
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVO  {
    private Integer id;
    private Integer userid;
    private String username;
    private String avatar;
    private String content;
    private LocalDateTime date;
    private Integer likeNum;
    private Boolean isLike;
    List<CommentDTO> children;
}
