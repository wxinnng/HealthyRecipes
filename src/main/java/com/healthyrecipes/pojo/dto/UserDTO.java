package com.healthyrecipes.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author:86198
 * @DATE:2024/1/21 16:04
 * @DESCRIPTION:
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {
  private static final long serialVersionUID = 1L;
  private Integer id;
  private String username;
  private String email;
  private String password;

}
