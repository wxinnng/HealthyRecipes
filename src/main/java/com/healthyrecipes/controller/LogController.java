package com.healthyrecipes.controller;

import com.healthyrecipes.common.constant.MessageConstant;
import com.healthyrecipes.common.result.ResultJson;
import com.healthyrecipes.common.utils.AliOssUtil;
import com.healthyrecipes.exception.BusinessException;
import com.healthyrecipes.pojo.dto.LogCommentDTO;
import com.healthyrecipes.pojo.dto.LogDTO;
import com.healthyrecipes.pojo.entity.LogComment;
import com.healthyrecipes.pojo.entity.LogContent;
import com.healthyrecipes.pojo.entity.Topic;
import com.healthyrecipes.pojo.query.LogCommentQuery;
import com.healthyrecipes.pojo.query.LogQuery;
import com.healthyrecipes.pojo.vo.LogUserVO;
import com.healthyrecipes.service.LogService;
import com.healthyrecipes.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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

    @Autowired
    private UserService userService;

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
            return ResultJson.error("服务器异常!");
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
        log.info("上传图片 {} {}", id,images);
        try {
            logService.upImages(images,id);
            return ResultJson.success("图片上传成功！");
        } catch (BusinessException e) {
            log.error(e.getMessage());
            return ResultJson.error("图片上传失败！");
        } catch (Exception e) {
            log.error(e.getMessage());
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
    public ResultJson<List<LogUserVO>> getLogList(@RequestBody LogQuery logQuery){
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
    
    /**
     *  对饮食记录发表评论
     * @param logComment 评论属性
     * @return 成功则在data中返回成功信息, 失败会根据情况返回对应错误信息
     */
    @PostMapping("/postComment")
    @ApiOperation("发表评论")
    public ResultJson<String> addLogComment(@RequestBody LogComment logComment){
        log.info("对饮食记录发表评论:{}", logComment);

        try {

            if(logComment.getLogId() == null || logComment.getLogId() <= 0
                    || logComment.getUserId() == null || logComment.getUserId() <= 0
                    || (logComment.getParentCommentId() != null && logComment.getParentCommentId() <= 0)){
                log.warn("id异常:{}", logComment);
                return ResultJson.error("id异常");
            }

            if (!StringUtils.hasLength(logComment.getContent())){
                log.warn("评论内容为空:{}", logComment);
                return ResultJson.error("评论内容不能为空");
            }

            return logService.addLogComment(logComment);

        }catch (Exception e){
            log.error("{}",e.getMessage());
            return ResultJson.error("内部发生异常");
        }
    }

    /**
     * @description: 获得log详情
     * @param: [java.lang.Integer]
     * @return: com.healthyrecipes.common.result.ResultJson<com.healthyrecipes.pojo.dto.LogDTO>
     */
    @GetMapping("/getLog")
    @ApiOperation("获得log详情")
    public ResultJson<LogDTO> getLogDetails(@RequestParam Integer id){
        if(id == null || id <= 0){
            log.warn("id异常:{}", id);
            return ResultJson.error("id异常");
        }
        return logService.getLogDetails(id);
    }

    /**
     * @description: 点赞或者取消点赞
     * @param: [java.lang.Integer, java.lang.Integer]
     * @return: com.healthyrecipes.common.result.ResultJson<java.lang.String>
     */
    @GetMapping("/dolike")
    @ApiOperation("点赞或者取消点赞")
    public ResultJson<String> doLike(@RequestParam Integer userid,@RequestParam Integer logCommentId){
        log.info("点赞 {} -> {}",userid,logCommentId);
        try{
            userService.doLike(userid,logCommentId, MessageConstant.COMMENT_TYPE_LOG);
            return ResultJson.success("操作成功！");
        }catch (Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("服务器异常！");
        }
    }

    @GetMapping("/getComment")
    public ResultJson<LogCommentDTO> getLogComments(LogCommentQuery logCommentQuery){
        try{

            return logService.getLogComments(logCommentQuery);
        }catch (Exception e){
            log.error("发生异常:{}", logCommentQuery);
            return ResultJson.error("内部发生异常");
        }
    }


}
