package com.healthyrecipes.pojo.entity;

import com.healthyrecipes.pojo.dto.FoodDTO;
import com.healthyrecipes.pojo.dto.MessageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author:86198
 * @DATE:2024/3/3 21:26
 * @DESCRIPTION: calorieResult
 * @VERSION:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalorieResult {
    private List<Object> calories;
    private List<MessageDTO> breakfast;
    private List<MessageDTO> lunch;
    private List<MessageDTO> dinner;
}
