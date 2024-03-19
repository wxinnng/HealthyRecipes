package com.healthyrecipes.service;

import com.healthyrecipes.pojo.entity.LogContent;
import com.healthyrecipes.pojo.entity.Topic;
import com.healthyrecipes.pojo.query.LogQuery;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author:86198
 * @DATE:2024/3/17 18:26
 * @DESCRIPTION: Log Service
 * @VERSION:1.0
 */
public interface LogService {

    List<LogContent> getLogList(LogQuery logQuery);

    void upImages(MultipartFile[] images, Integer userid) ;

    List<Topic> getTopicList(Topic topic);

    
    Boolean update(Integer id, String content);
}
