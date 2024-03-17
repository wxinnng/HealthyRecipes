package com.healthyrecipes.service.Impl;

import com.healthyrecipes.mapper.DishMapper;
import com.healthyrecipes.mapper.FoodMapper;
import com.healthyrecipes.pojo.entity.FoodDishDTO;
import com.healthyrecipes.pojo.vo.FoodVO;
import com.healthyrecipes.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author:86198
 * @DATE:2024/3/2 18:19
 * @DESCRIPTION:  Admin Service implement
 * @VERSION:1.0
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private FoodMapper foodMapper;

    @Autowired
    private DishMapper dishMapper;


    /**
     * @description: 填充菜品信息
     * @param: []
     * @return: com.healthyrecipes.pojo.entity.FoodDishDTO
     */
    @Override
    public FoodDishDTO fullData() {
        FoodDishDTO ans = new FoodDishDTO();


        /*填充信息*/
        ans.setFoodNum(foodMapper.getFoodNum());
        ans.setCategory(foodMapper.getCategoryNum());
//        ans.setDishNum();
        return ans;
    }


}
