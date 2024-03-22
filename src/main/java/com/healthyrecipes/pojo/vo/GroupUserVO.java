package com.healthyrecipes.pojo.vo;

import com.healthyrecipes.pojo.dto.UserMemberDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author:86198
 * @DATE:2024/3/21 8:44
 * @DESCRIPTION:GroupUserVO
 * @VERSION:1.0
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class GroupUserVO {
    private Integer id;
    private String groupName;
    private Integer ownerId;
    private Integer curNum;
    private Integer groupSize;
    private String introduce;
    private String codeInfo;
    private LocalDateTime createTime;
    private List<UserMemberDTO> members;
}
