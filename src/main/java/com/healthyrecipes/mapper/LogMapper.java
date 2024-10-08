package com.healthyrecipes.mapper;

import com.healthyrecipes.pojo.dto.LogCommentDTO;
import com.healthyrecipes.pojo.dto.LogDTO;
import com.healthyrecipes.pojo.entity.LogComment;
import com.healthyrecipes.pojo.entity.LogContent;
import com.healthyrecipes.pojo.entity.Topic;
import com.healthyrecipes.pojo.query.LogCommentQuery;
import com.healthyrecipes.pojo.query.LogQuery;
import com.healthyrecipes.pojo.vo.LogUserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author:86198
 * @DATE:2024/3/17 20:54
@@ -10,4 +15,17 @@ import org.apache.ibatis.annotations.Mapper;
 */
@Mapper
public interface LogMapper {
    /**
     * 分页查询log列表
     * @param logQuery 查询条件
     * @return java.util.List<com.healthyrecipes.pojo.entity.LogContent>
     */
    List<LogUserVO> queryLogByPage(LogQuery logQuery);

    /**
     * 获取Topic
     * @param topic 查询条件
     * @return java.util.List<com.healthyrecipes.pojo.entity.topic>
     */
    List<Topic> getTopicList(Topic topic);

    /**
     * @description: 插入一个Log
     * @param: [com.healthyrecipes.pojo.entity.LogContent]
     * @return: void
     */
    void insertALog(LogContent logContent);

    /**
     * @description: 更新log信息
     * @param: [com.healthyrecipes.pojo.query.LogQuery]
     * @return: void
     */
    void updateLog(LogContent logContent);

    
    void insertLogComment(LogComment logComment);

    boolean checkLogId(Integer userId);

    boolean checkUserId(Integer userId);

    boolean checkParentCommentId(Integer parentCommentId);

    LogDTO getLogWithUserInfoById(Integer logId);

    void operateForLike(Integer commentId, int operator);

    List<LogCommentDTO.Comment> queryTopLevelComments(LogCommentQuery logCommentQuery);
}