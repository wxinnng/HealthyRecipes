package com.healthyrecipes.controller;

import com.healthyrecipes.common.result.ResultJson;
import com.healthyrecipes.pojo.dto.FoodDTO;
import com.healthyrecipes.pojo.entity.CalorieResult;
import com.healthyrecipes.pojo.entity.Category;
import com.healthyrecipes.pojo.entity.Save;
import com.healthyrecipes.pojo.vo.*;
import com.healthyrecipes.service.FoodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author:86198
 * @DATE:2024/2/19 14:56
 * @DESCRIPTION: food接口
 * @VERSION:1.0
 */
@RestController
@Api("food接口")
@RequestMapping("/food")
@Slf4j
public class FoodController {

    @Autowired
    private FoodService foodService;

    /**
     * @description: 获取食品列表
     * @param: []
     * @return: com.healthyrecipes.common.result.ResultJson<java.util.List<com.healthyrecipes.pojo.entity.Food>>
     */
    @PostMapping("/list")
    @ApiOperation("食品列表")
    public ResultJson<PageVO> getFoodList(@RequestBody FoodDTO foodDTO){
        log.info("获取食品列表 {}",foodDTO);
        ResultJson<PageVO> result = new ResultJson<>();
        try{
            PageVO foodList = foodService.getFoodList(foodDTO);
            result.setData(foodList);
            return result;
        }catch(Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("服务器异常");
        }
    }

    /**
     * @description: 获取食品种类列表
     * @param: []
     * @return: com.healthyrecipes.common.result.ResultJson<java.util.List<com.healthyrecipes.pojo.entity.Category>>
     */
    @GetMapping("/category")
    @ApiOperation("食品种类")
    public ResultJson<List<Category>> getCategoryList(){
        log.info("获取食品种类");
        ResultJson<List<Category>> result = new ResultJson<>();
        try{
            result.setData(foodService.getCategoryList());
            return result;
        }catch(Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("服务器异常");
        }
    }



    @PostMapping("/cansave")
    @ApiOperation("判断能否收藏")
    public ResultJson<Boolean> canSave(@RequestBody Save save){
        log.info("判断是否能收藏{}",save);
        try{
            return ResultJson.success(foodService.canSave(save));
        } catch(Exception e){
            System.err.print(e.getMessage());
            return ResultJson.error("服务器异常！");
        }
    }



    /**
     * @description: 添加收藏
     * @param: [com.healthyrecipes.pojo.entity.Save]
     * @return: com.healthyrecipes.common.result.ResultJson<java.lang.String>
     */
    @PostMapping("/dosave")
    @ApiOperation("添加收藏")
    public ResultJson<String> doSave(@RequestBody Save save){
        log.info("添加收藏");
        try{
            foodService.addSave(save);
            return ResultJson.success("收藏成功！");
        }catch(Exception e){
            System.err.println(e.getLocalizedMessage());
            return ResultJson.error("服务器异常！");
        }
    }


    /**
     * @description: 获得收藏内容
     * @param: [java.lang.Integer]
     * @return: com.healthyrecipes.common.result.ResultJson<com.healthyrecipes.pojo.vo.SaveVO>
     */
    @GetMapping("/save/{userid}")
    @ApiOperation("获得收藏内容")
    public ResultJson<SaveVO> saveList(@PathVariable Integer userid){
        log.info("获得收藏列表 {}",userid);
        try{
            SaveVO saves = foodService.getSaveListByUserid(userid);
            return ResultJson.success(saves);
        }catch (Exception e) {
            System.err.println(e.getMessage());
            return ResultJson.error("服务器异常");
        }
    }


    /**
     * @description: 删除收藏
     * @param: [java.lang.Integer]
     * @return: com.healthyrecipes.common.result.ResultJson<java.lang.String>
     */
    @PostMapping("/rmsave")
    @ApiOperation("删除收藏")
    public ResultJson<String> rmSave(@RequestBody RmSaveVO rmSaveVO){
        log.info("删除收藏");
        try{
            foodService.doRmSave(rmSaveVO);
            return ResultJson.success("删除成功！");
        }catch (Exception e){
            System.err.println(e.getLocalizedMessage());
            return ResultJson.error("服务器异常！");
        }
    }

    /**
     * @description: 随机推荐接口
     * @param: []
     * @return: com.healthyrecipes.common.result.ResultJson<java.util.List<com.healthyrecipes.pojo.vo.Suggestion>>
     */
    @GetMapping("/recommend")
    @ApiOperation("随机推荐十条食品")
    public ResultJson<List<Suggestion>> getSuggestion(){
        log.info("获得随机推荐的十个食品");
        try{
            return ResultJson.success(foodService.getTenRandomFoods());
        }catch(Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("服务器异常");
        }
    }

    @PostMapping("/intake")
    @ApiOperation("设置热量推荐")
    public ResultJson<FoodVO> setIntake(@RequestBody IntakeVO intakeVO){
        log.info("设置热量推荐 {}",intakeVO);
        try{
            return ResultJson.success(foodService.setRecommendedIntake(intakeVO));
        }catch( Exception e ){
            System.err.println(e.getMessage());
            return ResultJson.error("服务器异常");
        }
    }


    /**
     * @description: 获得热量推荐
     * @param: []
     * @return: com.healthyrecipes.common.result.ResultJson<com.healthyrecipes.pojo.vo.FoodVO>
     */
    @GetMapping("/getrecommend/{id}")
    @ApiOperation("获得热量推荐")
    public ResultJson<FoodVO> getRecommend(@PathVariable Integer id){
        log.info("获得热量推荐 {}",id);
        try{
            FoodVO result = foodService.getRecommendedIntake(id);
            return ResultJson.success(result);
        }catch(Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("服务器异常");
        }
    }


    /**
     * @description: 获取热量详情
     * @param: [java.lang.Integer]
     * @return: com.healthyrecipes.common.result.ResultJson<com.healthyrecipes.pojo.entity.CalorieResult>
     */
    @GetMapping("/getcalorie/{id}")
    @ApiOperation("获得热量")
    public ResultJson<CalorieResult> getUserCalorie(@PathVariable Integer id){
        log.info("获得用户热量id {}",id);
        try{
            CalorieResult result = foodService.getUserCalorie(id);
            return ResultJson.success(result);
        }catch(Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("服务器异常!");
        }
    }


    /**
     * @description: 设置热量
     * @param: [com.healthyrecipes.pojo.vo.CalorieVO]
     * @return: com.healthyrecipes.common.result.ResultJson<java.lang.String>
     */
    @PostMapping("/setcalorie")
    @ApiOperation("添加热量")
    public ResultJson<String> setUserCalorie(@RequestBody CalorieVO calorieVO){
        log.info("添加热量 {} ",calorieVO.getFoodId());
        try{
            foodService.addCalorie(calorieVO);
            return ResultJson.success("添加成功！");
        }catch (Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("服务器异常!");
        }
    }

}
