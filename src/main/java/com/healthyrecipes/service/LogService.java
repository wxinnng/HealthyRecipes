package com.healthyrecipes.service;

import com.healthyrecipes.common.result.ResultJson;
import com.healthyrecipes.pojo.dto.LogCommentDTO;
import com.healthyrecipes.pojo.dto.LogDTO;
import com.healthyrecipes.pojo.entity.LogComment;
import com.healthyrecipes.pojo.entity.LogContent;
import com.healthyrecipes.pojo.entity.Topic;
import com.healthyrecipes.pojo.query.LogCommentQuery;
import com.healthyrecipes.pojo.query.LogQuery;
import com.healthyrecipes.pojo.vo.LogUserVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author:86198
 * @DATE:2024/3/17 18:26
 * @DESCRIPTION: Log Service
 * @VERSION:1.0
 */
public interface LogService {

    List<LogUserVO> getLogList(LogQuery logQuery);

    void upImages(MultipartFile[] images, Integer userid) ;

    List<Topic> getTopicList(Topic topic);

    Integer insertALog(LogContent logContent);

    List<Object> getRecords(Integer userid);

    
    ResultJson<String> addLogComment(LogComment logComment);

    ResultJson<LogDTO> getLogDetails(Integer id);

    ResultJson<LogCommentDTO> getLogComments(LogCommentQuery logCommentQuery);
}
