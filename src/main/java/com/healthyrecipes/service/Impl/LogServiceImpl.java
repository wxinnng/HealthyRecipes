package com.healthyrecipes.service.Impl;

import com.github.pagehelper.PageHelper;
import com.healthyrecipes.common.utils.AliOssUtil;
import com.healthyrecipes.exception.BusinessException;
import com.healthyrecipes.mapper.LogMapper;
import com.healthyrecipes.pojo.entity.LogContent;
import com.healthyrecipes.pojo.entity.Topic;
import com.healthyrecipes.pojo.query.LogQuery;
import com.healthyrecipes.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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


    @Autowired
    private LogMapper logMapper;

    @Override
    public List<LogContent> getLogList(LogQuery logQuery) {
        // 分页查询设置
        PageHelper.startPage(logQuery.getPageNum(), logQuery.getPageSize());
        // 查询并返回数据
        return logMapper.queryLogByPage(logQuery);
    }


    @Override
    public void upImages(MultipartFile[] images, Integer userid) {

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

            imageBuilder.append(objectName).append("\n");
        }

        /*
            TODO: *2* 在LogMapper中创建一个新的Update方法，更新Log表,最好可以对Log表中的所有的字段都能更新
            加一个判断，如果不为null，就不更新，如果不清楚的话，就看看别的xml文件中update方法怎么写的。
        */

    }

    @Override
    public List<Topic> getTopicList(Topic topic) {
        return logMapper.getTopicList(topic);
    }
}
