package com.healthyrecipes.service.Impl;

import com.github.pagehelper.PageHelper;
import com.healthyrecipes.common.constant.MessageConstant;
import com.healthyrecipes.common.result.ResultJson;
import com.healthyrecipes.common.utils.AliOssUtil;
import com.healthyrecipes.common.utils.RedisUtil;
import com.healthyrecipes.exception.BusinessException;
import com.healthyrecipes.mapper.LogMapper;
import com.healthyrecipes.pojo.dto.LogCommentDTO;
import com.healthyrecipes.pojo.dto.LogDTO;
import com.healthyrecipes.pojo.entity.LogComment;
import com.healthyrecipes.pojo.entity.LogContent;
import com.healthyrecipes.pojo.entity.Topic;
import com.healthyrecipes.pojo.query.LogCommentQuery;
import com.healthyrecipes.pojo.query.LogQuery;
import com.healthyrecipes.pojo.vo.LogUserVO;
import com.healthyrecipes.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
/**
 * @Author:86198
 * @DATE:2024/3/17 18:27
 * @DESCRIPTION: Log Service Impl
 * @VERSION:1.0
 */
@Service
@Slf4j
public class LogServiceImpl implements LogService {


    @Autowired
    private AliOssUtil aliOssUtil;  //阿里云OOS

    @Resource
    private RedisUtil redisUtil;   // RedisUtil

    @Autowired
    private LogMapper logMapper;

    @Override
    public List<LogUserVO> getLogList(LogQuery logQuery) {
        if(logQuery.getPageNum() != null && logQuery.getPageSize() != null){
            PageHelper.startPage(logQuery.getPageNum(), logQuery.getPageSize());
        }
        // 查询并返回数据
        return logMapper.queryLogByPage(logQuery);
    }


    @Override
    public void upImages(MultipartFile[] images, Integer id) {

        //images中的全部内容
        StringBuilder imageBuilder = new StringBuilder();  //拼接好，放到数据库中。

        //将图片都放到OOS中去
        for(int i = 0 ;i < images.length; i++){
            //原始文件名
            String originalFilename = images[i].getOriginalFilename();

            //截取原始文件名的后缀
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新文件名称
            String objectName = UUID.randomUUID().toString() + extension;
            //文件的请求路径

            try {
                //拼接路径
                imageBuilder.append(aliOssUtil.upload(images[i].getBytes(), objectName));
            } catch (IOException e) {
                throw new BusinessException("图片上传失败！");
            }

            imageBuilder.append("|");

            LogContent logContent = new LogContent();
            logContent.setImages(imageBuilder.toString());
            logContent.setId(id);
            //放入到数据库。
            logMapper.updateLog(logContent);
        }

    }
    @Override
    public Integer insertALog(LogContent logContent){
        logContent.setDate(LocalDateTime.now());
        logMapper.insertALog(logContent);

        Integer oldValue = null;

        //记录到Redis中去
        if(logContent.getTopicId() == 1){
            //正常记录
            oldValue = (Integer) redisUtil.lget(MessageConstant.REDIS_LOG_KEY + logContent.getUserid(), 1);
        }else{
            //吃多了
            oldValue = (Integer) redisUtil.lget(MessageConstant.REDIS_LOG_KEY + logContent.getUserid(), 2);
        }

        Integer allNum = (Integer) redisUtil.lget(MessageConstant.REDIS_LOG_KEY + logContent.getUserid(), 0);

        //更新redis中的内容
        redisUtil.lset(MessageConstant.REDIS_LOG_KEY + logContent.getUserid(),0,allNum + 1);
        redisUtil.lset(MessageConstant.REDIS_LOG_KEY + logContent.getUserid(),logContent.getTopicId(),oldValue + 1);

        return logContent.getId();
    }

    @Override
    public List<Topic> getTopicList(Topic topic) {
        return logMapper.getTopicList(topic);
    }

    @Override
    public List<Object> getRecords(Integer userid) {
        return redisUtil.lrange(MessageConstant.REDIS_LOG_KEY + userid, 0, -1);
    }

    
    @Override
    public ResultJson<String> addLogComment(LogComment logComment) {
        // 查看userId, LogId是否存在于数据库中
        if(!checkUserId(logComment.getUserId()) || !checkLogId(logComment.getLogId())){
            log.warn("userId或logId不存在于数据库中:{}", logComment);
            return ResultJson.error("用户或话题不存在");
        }

        // 如果有parentCommentId, 进行检查
        if (logComment.getParentCommentId() != null
                && checkParentCommentId(logComment.getParentCommentId())){
            log.warn("parentCommentId不存在于数据库中:{}", logComment);
            return ResultJson.error("评论不存在");
        }

        logMapper.insertLogComment(logComment);
        return ResultJson.success("发布评论成功");
    }

    /**
     * 用于查询饮食圈在数据库中是否存在且有效
     * @param logId 饮食记录id
     * @return true表示存在且有效
     */
    private boolean checkLogId(Integer logId){
        return logMapper.checkLogId(logId);
    }

    /**
     * 用于查询饮食圈在数据库中是否存在且有效
     * @param userId 用户Id
     * @return true表示存在且有效
     */
    private boolean checkUserId(Integer userId){
        return logMapper.checkUserId(userId);
    }

    /**
     * 用于查询父评论在数据库中是否存在且有效
     * @param parentCommentId 父评论Id
     * @return true表示存在且有效
     */
    private boolean checkParentCommentId(Integer parentCommentId){
        return logMapper.checkParentCommentId(parentCommentId);
    }

    @Override
    public ResultJson<LogDTO> getLogDetails(Integer id) {
        return ResultJson.success(logMapper.getLogWithUserInfoById(id));
    }

    @Override
    public ResultJson<LogCommentDTO> getLogComments(LogCommentQuery logCommentQuery) {
        // 创建返回值
        LogCommentDTO logCommentDTO = new LogCommentDTO();

        // 如果没有提供父评论id, 那么就是要提供dislikeNum和likeNum
        if(logCommentQuery.getParentCommentId() == null){
            // 根据id找到log, 放入dislikeNum和likeNum
            LogQuery logQuery = new LogQuery();
            logQuery.setId(logCommentQuery.getLogId());
            List<LogUserVO> logList = getLogList(logQuery);
            logCommentDTO.setLikeNum(logList.get(0).getLikeNum());
            logCommentDTO.setDislikeNum(logList.get(0).getDisLikeNum());
        }


        // 查找数据, 会根据parentCommentsId是否提供来判断查询的是父评论还是子评论
        List<LogCommentDTO.Comment> logComments = logMapper.queryTopLevelComments(logCommentQuery);

        // 将查找到的评论数据放入返回值
        logCommentDTO.setLogComments(logComments);

        return ResultJson.success(logCommentDTO);
    }
}
