package com.healthyrecipes.service;

import com.github.pagehelper.Page;
import com.healthyrecipes.pojo.dto.FoodDTO;
import com.healthyrecipes.pojo.entity.CalorieResult;
import com.healthyrecipes.pojo.entity.Category;
import com.healthyrecipes.pojo.entity.Food;
import com.healthyrecipes.pojo.entity.Save;
import com.healthyrecipes.pojo.vo.*;

import java.util.List;

/**
 * @Author:86198
 * @DATE:2024/2/18 19:34
 * @DESCRIPTION: 食品接口
 * @VERSION:1.0
 */
public interface FoodService {

    /**
     * @description: 获得食品列表
     * @param: []
     * @return: java.util.List<com.healthyrecipes.pojo.entity.Food>
     */
    PageVO getFoodList(FoodDTO foodDTO);

    /**
     * @description: 获得种类列表
     * @param: []
     * @return: java.util.List<com.healthyrecipes.pojo.entity.Category>
     */
    List<Category> getCategoryList();

    /**
     * @description: 修改种类
     * @param: [com.healthyrecipes.pojo.entity.Category]
     * @return: void
     */
    void modifyCategory( Category category);

    /**
     * @description: 添加种类
     * @param: [com.healthyrecipes.pojo.entity.Category]
     * @return: void
     */
    void addCategory(Category category);

    /**
     * @description: 通过id删除种类
     * @param: [java.lang.Integer]
     * @return: void
     */
    void deleteCategoryById(Integer id);

    /**
     * @description: 通过获得用户的收藏列表
     * @param: [java.lang.Integer]
     * @return: com.healthyrecipes.pojo.vo.SaveVo
     */
    SaveVO getSaveListByUserid(Integer userid);

    /**
     * @description: 随机获得10条食品
     * @param: []
     * @return: java.util.List<com.healthyrecipes.pojo.vo.Suggestion>
     */
    List<Suggestion> getTenRandomFoods();

    /**
     * @description: 获得摄入建议
     * @param: [com.healthyrecipes.pojo.vo.IntakeVO]
     * @return: com.healthyrecipes.pojo.vo.FoodVO
     */
    FoodVO setRecommendedIntake(IntakeVO intakeVO);


    /**
     * @description: 获得用户的热量推荐
     * @param: [java.lang.Integer]
     * @return: com.healthyrecipes.pojo.entity.Food
     */
    FoodVO getRecommendedIntake(Integer id);

    /**
     * @description: 获得热量详情
     * @param: [java.lang.Integer]
     * @return: com.healthyrecipes.pojo.entity.CalorieResult
     */
    CalorieResult getUserCalorie(Integer userid);

    /**
     * @description: 添加热量
     * @param: [com.healthyrecipes.pojo.vo.CalorieVO]
     * @return: void
     */
    void addCalorie(CalorieVO calorieVO);



    /**
     * @description: 删除收藏
     * @param: [java.lang.Integer]
     * @return: void
     */
    void doRmSave(RmSaveVO rmSaveVO);

    /**
     * @description: 添加一条收藏
     * @param: [com.healthyrecipes.pojo.entity.Save]
     * @return: void
     */
    void addSave(Save save);

    /**
     * @description: 修改food的url
     * @param: [java.lang.String]
     * @return: void
     */
    void updateFood(Food food);

    /**
     * @description: 插入一条数据
     * @param: [com.healthyrecipes.pojo.entity.Food]
     * @return: void
     */
    void insertAFood(Food food);

    /**
     * @description: 删除一条数据
     * @param: [java.lang.Integer]
     * @return: void
     */
    void deleteFood(Integer id);

    /**
     * @description: 判断能不能被收藏
     * @param: [com.healthyrecipes.pojo.entity.Save]
     * @return: boolean
     */
    boolean canSave(Save save);
}
