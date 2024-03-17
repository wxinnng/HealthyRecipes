package com.healthyrecipes.controller;

import com.healthyrecipes.common.result.ResultJson;
import com.healthyrecipes.pojo.dto.DishDTO;
import com.healthyrecipes.pojo.entity.BaiduDish;
import com.healthyrecipes.pojo.entity.Dish;
import com.healthyrecipes.pojo.entity.Result;
import com.healthyrecipes.pojo.vo.DishPageVO;
import com.healthyrecipes.pojo.vo.RandDishVO;
import com.healthyrecipes.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author:86198
 * @DATE:2024/1/27 21:18
 * @DESCRIPTION:
 * @VERSION:1.0
 */
@RestController
@Slf4j
@Api("菜品相关")
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * @description: 菜品识别
     * @param: [org.springframework.web.multipart.MultipartFile]
     * @return: com.healthyrecipes.common.result.ResultJson<com.healthyrecipes.pojo.entity.BaiduDish>
     */
    @PostMapping("/parse")
    @ApiOperation("菜品识别")
    public ResultJson<BaiduDish> parse(@RequestBody MultipartFile image){
        BaiduDish result = dishService.parseImage(image);
        if (result == null)
            return ResultJson.error("识别失败");
        ResultJson<BaiduDish> resultJson = new ResultJson<>();
        resultJson.setData(result);
        resultJson.setMsg("识别成功");
        resultJson.setCode(1);
        return resultJson;
    }


    /**
     * @description: 获得Dish列表
     * @param: [com.healthyrecipes.pojo.dto.DishDTO]
     * @return: com.healthyrecipes.common.result.ResultJson<com.healthyrecipes.pojo.vo.DishPageVO>
     */
    @PostMapping("/getdishes")
    @ApiOperation("获得Dish列表")
    public ResultJson<DishPageVO> getDishList(@RequestBody DishDTO dishDTO){
        log.info("获得Dish列表");
        try{
            return ResultJson.success(dishService.getDishList(dishDTO));
        }catch (Exception e){
            System.err.println(e.getMessage()+e.getLocalizedMessage());
            return ResultJson.error("服务器异常！");
        }
    }

    /**
     * @description: 随机获得三个Dish
     * @param: []
     * @return: com.healthyrecipes.common.result.ResultJson<java.util.List<com.healthyrecipes.pojo.vo.RandDishVO>>
     */
    @GetMapping("/recommend")
    @ApiOperation("随机获得三个Dish")
    public ResultJson<List<RandDishVO>> getRandomTreeDish(){
        log.info("随机获得三个Dish");
        try{
            return ResultJson.success(dishService.getThreeRandDishes());
        }catch(Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("服务器异常！");
        }
    }
}
