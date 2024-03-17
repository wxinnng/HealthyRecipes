package com.healthyrecipes.pojo.vo;

import com.healthyrecipes.pojo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author:86198
 * @DATE:2024/1/21 19:48
 * @DESCRIPTION:
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String token;
    private User user;
}
