package com.healthyrecipes.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author:86198
 * @DATE:2024/3/18 17:53
 * @DESCRIPTION: Group entity
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    private Integer id;
    private String groupName;
    private Integer ownerId;
    private Integer curNum;
    private Integer groupSize;
    private String introduce;
    private String invitationCode;
    private LocalDateTime createTime;
}
