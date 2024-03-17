package com.healthyrecipes.mapper;

import com.github.pagehelper.Page;
import com.healthyrecipes.pojo.dto.FoodDTO;
import com.healthyrecipes.pojo.dto.IntakeDTO;
import com.healthyrecipes.pojo.dto.MessageDTO;
import com.healthyrecipes.pojo.entity.Category;
import com.healthyrecipes.pojo.entity.Food;
import com.healthyrecipes.pojo.entity.Save;
import com.healthyrecipes.pojo.vo.FoodVO;
import com.healthyrecipes.pojo.vo.RmSaveVO;
import com.healthyrecipes.pojo.vo.Suggestion;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * @Author:86198
 * @DATE:2024/2/18 19:23
 * @DESCRIPTION:
 * @VERSION:1.0
 */
@Mapper
public interface FoodMapper {

    /**
     * @description: 查询对应种类的下的食品
     * @param: [java.lang.Integer]
     * @return: java.util.List<com.healthyrecipes.pojo.entity.Food>
     */
    Page<Food> getFoodList(FoodDTO foodDTO);

    /**
     * @description: 获得所有的种类信息
     * @param: []
     * @return: java.util.List<com.healthyrecipes.pojo.entity.Category>
     */
    List<Category> getCategoryList(Category category);

    /**
     * @description: 修改种类的标题
     * @param: [com.healthyrecipes.pojo.entity.Category]
     * @return: void
     */
    @Update("update category set title = #{title} where id = #{id}")
    void modifyCategory(Category category);

    /**
     * @description: 添加种类
     * @param: [com.healthyrecipes.pojo.entity.Category]
     * @return: void
     */
    @Insert("insert into category (title) values (#{title})")
    void addCategory(Category category);

    /**
     * @description: 通过id删除category
     * @param: [java.lang.Integer]
     * @return: void
     */
    @Delete("delete  from category where id = #{id}")
    void deleteCategoryById(Integer id);

    /**
     * @description: 通过ids获得foodList
     * @param: [java.util.List<java.lang.Integer>]
     * @return: void
     */
    List<Food> getFoodListByIds(List<Integer> ids);

    /**
     * @description: 随机获得10条数据
     * @param: []
     * @return: java.util.List<com.healthyrecipes.pojo.vo.Suggestion>
     */
    List<Suggestion> getRandomFoods(int[] nums);

    /**
     * @description: 获得Food的数量
     * @param: []
     * @return: java.lang.Integer
     */
    @Select("select count(id) from food")
    Integer getFoodNum();


    /**
     * @description: 通过种类id获得对应下食物的数量
     * @param: [java.lang.Integer]
     * @return: java.lang.Integer
     */
    @Select("select food_num from category where id = #{id}")
    Integer getFoodNumById(Integer id);

    /**
     * @description: 获得Category的数量
     * @param: []
     * @return: java.lang.Integer
     */
    @Select("select count(id) from category")
    Integer getCategoryNum();

    /**
     * @description: 设置推荐热量
     * @param: [com.healthyrecipes.pojo.vo.FoodVO]
     * @return: void
     */
    void setSuggestion(FoodVO foodVO);


    /**
     * @description: 插入一条内容
     * @param: [java.lang.Integer]
     * @return: void
     */
    @Insert("insert into suggestion (userid) values (#{userid})")
    void insertSuggestion(Integer userid);

    /**
     * @description: 获得suggestion
     * @param: [java.lang.Integer]
     * @return: java.util.List<com.healthyrecipes.pojo.dto.IntakeDTO>
     */
    List<FoodVO> getSuggestion(Integer userid);



    /**
     * @description: 获得详细的热量对应的食物信息
     * @param: [java.util.List<java.lang.Integer>]
     * @return: java.util.List<com.healthyrecipes.pojo.dto.MessageDTO>
     */
    List<MessageDTO> getMessage(List<Integer> ids);



    /**
     * @description: 删除收藏
     * @param: [java.lang.Integer]
     * @return: void
     */
    @Update("delete from save where userid=#{userid} and type =#{type} and food_id=#{foodId}")
    void doRmSave(RmSaveVO rmSaveVO);

    /**
     * @description: 更新食物信息
     * @param: [com.healthyrecipes.pojo.entity.Food]
     * @return: void
     */
    void updateFood(Food food);

    /**
     * @description: 插入一条数据
     * @param: [com.healthyrecipes.pojo.entity.Food]
     * @return: void
     */
    @Insert("insert into food (category_id,title,cellulose,calories,fat,carbohydrate,protein) " +
            "values (#{categoryId},#{title},#{cellulose},#{calories},#{fat},#{carbohydrate},#{protein})")
    void insertAFood(Food food);

    /**
     * @description: 删除食物
     * @param: [java.lang.Integer]
     * @return: void
     */

    @Delete("delete from food where id = #{id}")
    void deleteFood(Integer id);

    /**
     * @description: 判断能不能收藏
     * @param: [com.healthyrecipes.pojo.entity.Save]
     * @return: java.util.List<com.healthyrecipes.pojo.entity.Save>
     */
    List<Save> getCanSave(Save save);
}
