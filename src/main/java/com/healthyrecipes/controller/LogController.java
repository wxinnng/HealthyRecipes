package com.healthyrecipes.controller;

import com.healthyrecipes.common.result.ResultJson;
import com.healthyrecipes.common.utils.AliOssUtil;
import com.healthyrecipes.exception.BusinessException;
import com.healthyrecipes.pojo.entity.LogContent;
import com.healthyrecipes.pojo.entity.Topic;
import com.healthyrecipes.pojo.query.LogQuery;
import com.healthyrecipes.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @Author:86198
 * @DATE:2024/3/17 18:25
 * @DESCRIPTION: log controller
 * @VERSION:1.0
 */
@RestController
@RequestMapping("/log")
@Slf4j
@Api("logController")
public class LogController extends ABaseController {

    @Autowired
    private LogService logService;

    /**
     * @description: 接收图片以外的内容
     * @param: []
     * @return: com.healthyrecipes.common.result.ResultJson<java.lang.String>
     */
    @PostMapping("/uplog")
    @ApiOperation("上传log")
    public ResultJson<Object> upLogText(@RequestBody LogContent logContent) {
        log.info("上传log中文本内容 " +
                "{}", logContent);
        try{
            Integer logId = logService.insertALog(logContent);
            return ResultJson.success(logId);
        }catch(Exception e){
            log.error("{}",e.getMessage());
            return ResultJson.error(null);
        }
    }

    /**
     * @description: 上传log中的图片
     * @param: [org.springframework.web.multipart.MultipartFile, java.lang.Integer]
     * @return: com.healthyrecipes.common.result.ResultJson<java.lang.String>
     */
    @PostMapping("/upimg/{id}")
    @ApiOperation("更新图片")
    public ResultJson<String> upLogImage(@RequestBody MultipartFile[] images, @PathVariable Integer id) {
        log.info("上传图片 {}", id);
        try {
            logService.upImages(images,id);
            return ResultJson.success("图片上传成功！");
        } catch (BusinessException e) {
            log.error("阿里云OOS上传图片失败");
            return ResultJson.error("图片上传失败！");
        } catch (Exception e) {
            log.error("服务器异常！");
            return ResultJson.error("服务器异常！");
        }
    }

    /**
     * @description: 获取饮食圈内容
     * @param: [com.healthyrecipes.pojo.query.LogQuery]
     * @return: com.healthyrecipes.common.result.ResultJson<java.util.List<com.healthyrecipes.pojo.entity.LogContent>>
     */
    @PostMapping("/getlist")
    @ApiOperation("获得所有的Log列表")
    public ResultJson<List<LogContent>> getLogList(@RequestBody LogQuery logQuery){
        log.info("获得饮食圈的内容:{}",logQuery);

        try{
            return ResultJson.success(logService.getLogList(logQuery));
        }catch(Exception e){
            log.error("{}",e.getMessage());
            return ResultJson.error("服务器异常");
        }
    }

    /**
     * @description: 获得所有的topics
     * @param: [com.healthyrecipes.pojo.entity.Topic]
     * @return: com.healthyrecipes.common.result.ResultJson<java.util.List<com.healthyrecipes.pojo.entity.Topic>>
     */
    @PostMapping("/topics")
    @ApiOperation("获得所有的topics")
    public ResultJson<List<Topic>> getTopicList(@RequestBody Topic topic){

        log.info("获得Topic列表:{}", topic);
        try{
            return ResultJson.success(logService.getTopicList(topic));
        }catch (Exception e){
            log.error("{}",e.getMessage());
            return ResultJson.error("服务器异常");
        }
    }

    /**
     * @description: 获得饮食圈中的打卡记录
     * @param: [java.lang.Integer]
     * @return: com.healthyrecipes.common.result.ResultJson<java.util.List<java.lang.Object>>
     */
    @GetMapping("/record/{userid}")
    @ApiOperation("获得饮食圈记录")
    public ResultJson<List<Object>> getRecords(@PathVariable Integer userid){
        log.info("获得饮食圈记录 {}",userid);

        try{
            List<Object> records = logService.getRecords(userid);
            return ResultJson.success(records);
        }catch(Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("服务器异常!");
        }
    }
}
