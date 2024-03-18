package com.healthyrecipes.mapper;

import com.healthyrecipes.pojo.entity.LogContent;
import com.healthyrecipes.pojo.entity.Topic;
import com.healthyrecipes.pojo.query.LogQuery;
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
    List<LogContent> queryLogByPage(LogQuery logQuery);

    /**
     * 获取Topic
     * @param topic 查询条件
     * @return java.util.List<com.healthyrecipes.pojo.entity.topic>
     */
    List<Topic> getTopicList(Topic topic);
}