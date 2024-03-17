package com.healthyrecipes.pojo.entity;

import com.healthyrecipes.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author:86198
 * @DATE:2024/1/21 15:29
 * @DESCRIPTION:
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    private String email;
    private Double height;
    private Double weight;
    private String avatar;
    private Integer sex;
    private Date birth;
    private String habit;
    public UserMessage(String email){this.email = email;}
}
