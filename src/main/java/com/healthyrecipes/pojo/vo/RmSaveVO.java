package com.healthyrecipes.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * @Author:86198
 * @DATE:2024/3/10 16:51
 * @DESCRIPTION:  删除Save的VO
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RmSaveVO {
    private Integer userid;
    private Integer type;
    private Integer foodId;
}
