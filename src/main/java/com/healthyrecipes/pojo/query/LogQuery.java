package com.healthyrecipes.pojo.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Author:86198
 * @DATE:2024/3/17 21:04
 * @DESCRIPTION:  Log Query
 * @VERSION:1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogQuery implements Serializable {
    private Integer id;
    private Integer userid;
    private Integer topicId;
    private Integer pageSize; //用于分页查询
    private Integer pageNum;  //用于分页查询
    //有新的条件可以增加
}
