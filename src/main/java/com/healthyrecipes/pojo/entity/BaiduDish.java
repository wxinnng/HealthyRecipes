package com.healthyrecipes.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author:86198
 * @DATE:2024/1/27 21:12
 * @DESCRIPTION: 百度AI识物返回的结果对应的实体类
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaiduDish {
    private Long log_id;
    private Integer result_num;
    private List<Result> result;
    private String error_msg;
    private String error_code;
}
