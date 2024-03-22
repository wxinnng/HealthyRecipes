package com.healthyrecipes.service;

import com.github.pagehelper.Page;
import com.healthyrecipes.pojo.entity.Group;
import com.healthyrecipes.pojo.query.GroupQuery;
import com.healthyrecipes.pojo.vo.GroupUserVO;
import org.springframework.stereotype.Service;

/**
 * @Author:86198
 * @DATE:2024/3/18 18:08
 * @DESCRIPTION: 有关组队的所有服务
 * @VERSION:1.0
 */

public interface GroupService {

    /**
     * @description: 创建一个组队
     * @param: [com.healthyrecipes.pojo.entity.Group]
     * @return: void
     */
    void createGroup(Group group);

    /**
     * @description: 获得groupList，分页查询
     * @param: [com.healthyrecipes.pojo.query.GroupQuery]
     * @return: com.github.pagehelper.Page<com.healthyrecipes.pojo.entity.Group>
     */
    Page<Group> getList(GroupQuery query);

    /**
     * @description: 用户通过code进入小组
     * @param: [java.lang.Integer, java.lang.String]
     * @return: void
     */
    void joinInGroup(Integer userid, String code);

    /**
     * @description: ...
     * @param: [java.lang.Integer, java.lang.Integer]
     * @return: void
     */
    void delete(Integer userid, Integer groupId);

    /**
     * @description: 获得组队信息详情
     * @param: [java.lang.Integer]
     * @return: com.healthyrecipes.pojo.vo.GroupUserVO
     */
    GroupUserVO getDetail(Integer id);
}
