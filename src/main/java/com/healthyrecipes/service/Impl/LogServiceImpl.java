package com.healthyrecipes.service.Impl;

import com.github.pagehelper.PageHelper;
import com.healthyrecipes.common.constant.MessageConstant;
import com.healthyrecipes.common.utils.AliOssUtil;
import com.healthyrecipes.common.utils.RedisUtil;
import com.healthyrecipes.exception.BusinessException;
import com.healthyrecipes.mapper.LogMapper;
import com.healthyrecipes.pojo.entity.LogContent;
import com.healthyrecipes.pojo.entity.Topic;
import com.healthyrecipes.pojo.query.LogQuery;
import com.healthyrecipes.pojo.vo.LogUserVO;
import com.healthyrecipes.service.LogService;
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
public class LogServiceImpl implements LogService {


    @Autowired
    private AliOssUtil aliOssUtil;  //阿里云OOS

    @Resource
    private RedisUtil redisUtil;   // RedisUtil

    @Autowired
    private LogMapper logMapper;

    @Override
    public List<LogUserVO> getLogList(LogQuery logQuery) {
        // 分页查询设置
        PageHelper.startPage(logQuery.getPageNum(), logQuery.getPageSize());
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
}
