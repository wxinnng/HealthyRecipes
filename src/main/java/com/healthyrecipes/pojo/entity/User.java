package com.healthyrecipes.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;


/**
 * @Author:86198
 * @DATE:2023/12/30 14:38
 * @DESCRIPTION: 只是测试Jwt用的，使用的时候可以删掉
 *
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String username;
    private String password;
    private String email;
    private Integer status;
    private String phone;
    private Float height;
    private Float weight;
    private String avatar;
    private Integer sex;
    private LocalDate birth;
    private Integer exercise;
    private Integer target;
    public User (Integer id,String avatar){
        this.id = id;
        this.avatar = avatar;
    }
}
