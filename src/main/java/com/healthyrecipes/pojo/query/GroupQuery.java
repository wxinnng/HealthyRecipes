package com.healthyrecipes.pojo.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:86198
 * @DATE:2024/3/20 8:50
 * @DESCRIPTION: Group查询
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupQuery {
    private Integer id;
    private Integer ownerId;
    private String groupName;
    private String introduce;
    private String codeInfo;
    private Integer curNum;
    private Integer pageSize;
    private Integer pageNum;
}
