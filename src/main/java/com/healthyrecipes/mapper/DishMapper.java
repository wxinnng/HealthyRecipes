package com.healthyrecipes.mapper;

import com.github.pagehelper.Page;
import com.healthyrecipes.pojo.entity.Dish;
import com.healthyrecipes.pojo.vo.RandDishVO;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:86198
 * @DATE:2024/2/18 19:23
 * @DESCRIPTION: 菜品的Mapper
 * @VERSION:1.0
 */
@Mapper
public interface DishMapper {

    /**
     * @description: 通过id列表获得Dish
     * @param: []
     * @return: java.util.List<com.healthyrecipes.pojo.entity.Dish>
     */
    List<Dish> getDishByIds(List<Integer> ids);

    /**
     * @description: 获得DishList  分页查询
     * @param: []
     * @return: com.github.pagehelper.Page<com.healthyrecipes.pojo.entity.Dish>
     */
    Page<Dish> getDish(Integer id);


    /**
     * @description: 随机获得三个Dish
     * @param: [java.util.ArrayList<java.lang.Integer>]
     * @return: java.util.List<com.healthyrecipes.pojo.vo.RandDishVO>
     */
    List<RandDishVO> getDishByIdsNoDetail(ArrayList<Integer> ids);
}
