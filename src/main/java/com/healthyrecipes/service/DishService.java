package com.healthyrecipes.service;

import com.healthyrecipes.pojo.dto.DishDTO;
import com.healthyrecipes.pojo.entity.BaiduDish;
import com.healthyrecipes.pojo.entity.Dish;
import com.healthyrecipes.pojo.vo.DishPageVO;
import com.healthyrecipes.pojo.vo.RandDishVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author:86198
 * @DATE:2024/1/27 20:11
 * @DESCRIPTION:
 * @VERSION:1.0
 */
public interface DishService {

    /**
     * @description: 百度AI识物接口
     * @param: [org.springframework.web.multipart.MultipartFile]
     * @return: com.healthyrecipes.pojo.entity.BaiduDish
     */
    BaiduDish parseImage(MultipartFile image);


    /**
     * @description: 获得食品列表
     * @param: [com.healthyrecipes.pojo.dto.DishDTO]
     * @return: com.healthyrecipes.pojo.vo.DishPageVO
     */
    DishPageVO getDishList(DishDTO dishDTO);

    /**
     * @description: 随机获得三个dish
     * @param: []
     * @return: java.util.List<com.healthyrecipes.pojo.entity.Dish>
     */

    List<RandDishVO> getThreeRandDishes();
}
