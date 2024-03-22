package com.healthyrecipes.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author:86198
 * @DATE:2024/3/21 8:47
 * @DESCRIPTION:
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMemberDTO implements Serializable {
    private Integer id;
    private String username;
    private String avatar;
    private Integer isOwner;
}
