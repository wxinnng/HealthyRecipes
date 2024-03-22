package com.healthyrecipes.mapper;

import com.github.pagehelper.Page;
import com.healthyrecipes.pojo.dto.UserMemberDTO;
import com.healthyrecipes.pojo.entity.Group;
import com.healthyrecipes.pojo.entity.Member;
import com.healthyrecipes.pojo.query.GroupQuery;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author:86198
 * @DATE:2024/3/18 18:11
 * @DESCRIPTION: 有关Group 和 Member表的操作的Sql，都在这里写。
 * @VERSION:1.0
 */
@Mapper
public interface GroupMapper {


    /**
     * @description: 查询组队信息
     * @param: [com.healthyrecipes.pojo.query.GroupQuery]
     * @return: java.util.List<com.healthyrecipes.pojo.entity.Group>
     */
    Page<Group> getGroupList(GroupQuery groupQuery);

    /**
     * @description: 判断验证码是否重复
     * @param: [java.lang.String]
     * @return: java.lang.Integer
     */
    @Select("select count(id) from group where code ={code}")
    Integer invitationCodeIsOK(String code);

    /**
     * @description: 插入一个组
     * @param: [com.healthyrecipes.pojo.entity.Group]
     * @return: void
     */
    void createAGroup(Group group);

    /**
     * @description: 插入一条member信息
     * @param: [java.lang.Integer, java.lang.Integer, java.lang.Integer]
     * @return: void
     */
    @Insert("insert into member (user_id,group_id,is_owner) values (#{userId},#{groupId},#{isOwner})")
    void insertAMember(Integer userId, Integer groupId, Integer isOwner);

    /**
     * @description: 更新数据库中Group的信息
     * @param: [com.healthyrecipes.pojo.query.GroupQuery]
     * @return: void
     */
    void updateGroup(GroupQuery query);

    /**
     * @description: 获得MemberList
     * @param: [com.healthyrecipes.pojo.entity.Member]
     * @return: void
     */
    List<Member> getMemberList(Member member);

    /**
     * @description: 删除符合条件的Member
     * @param: [com.healthyrecipes.pojo.entity.Member]
     * @return: void
     */
    void deleteMember(Member member);

    /**
     * @description: 人数--
     * @param: [java.lang.Integer]
     * @return: void
     */
    @Update("update group_info set cur_num = cur_num - 1 where id = #{groupId}")
    void subCurNum(Integer groupId);

    /**
     * @description: 删除一个群聊
     * @param: [java.lang.Integer]
     * @return: void
     */
    @Delete("delete from group_info where id = #{groupId}")
    void deleteAGroup(Integer groupId);

    /**
     * @description: 删除一条Member信息
     * @param: [java.lang.Integer, java.lang.Integer]
     * @return: void
     */
    @Delete("delete from member where group_id = #{groupId} and user_id = #{userId}")
    void deleteAMember(Integer groupId, Integer userId);

    /**
     * @description: 删除对应GroupId下的所有Member
     * @param:
     * @return:
     */
    @Delete("delete from member where group_id = #{groupId}")
    void deleteAllMemberByGroupId(Integer groupId);

    /**
     * @description: 获得所有的ids，通过gourpId
     * @param: [java.lang.Integer]
     * @return: java.util.List<java.lang.Integer>
     */
    @Select("select user_id from member where group_id = #{groupId}")
    List<Integer> getMemberIds(Integer groupId);

    /**
     * @description: 获得Member和User中组合数据
     * @param: [java.lang.Integer]
     * @return: java.util.List<com.healthyrecipes.pojo.dto.UserMemberDTO>
     */
    List<UserMemberDTO> getMemberUserList(Integer id);
}
