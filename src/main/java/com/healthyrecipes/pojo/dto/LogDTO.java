package com.healthyrecipes.pojo.dto;

import lombok.Data;

@Data
public class LogDTO {
    Integer userId;
    String username;
    String avatar;
    String logContent;
    String logImages;
}