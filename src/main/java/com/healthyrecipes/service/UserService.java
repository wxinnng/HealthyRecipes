package com.healthyrecipes.service;

import com.healthyrecipes.exception.NoSuchCommentException;
import com.healthyrecipes.pojo.dto.UserDTO;
import com.healthyrecipes.pojo.entity.Admin;
import com.healthyrecipes.pojo.entity.CalorieResult;
import com.healthyrecipes.pojo.entity.Comment;
import com.healthyrecipes.pojo.entity.User;
import com.healthyrecipes.pojo.vo.CalorieVO;
import com.healthyrecipes.pojo.vo.CommentVO;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * @Author:86198
 * @DATE:2024/1/21 15:36
 * @DESCRIPTION:
 * @VERSION:1.0
 */
public interface UserService {

    /**
     * @description: 判断该邮箱是否已经注册
     * @param: [java.lang.String]
     * @return: boolean
     */
    boolean isEmailUsed(String email);

    /**
     * @description: 用户注册
     * @param: [com.healthyrecipes.pojo.dto.UserDTO]
     * @return: void
     */
    Integer register(UserDTO registerDTO);

    /**
     * @description: 用户登录
     * @param: [java.lang.String, java.lang.String]
     * @return: com.healthyrecipes.pojo.entity.User
     */
    User login(String email, String password);

    /**
     * @description: 通过id获得用户信息
     * @param: [java.lang.Long]
     * @return: com.healthyrecipes.pojo.entity.User
     */
    User getUserMessageById(Integer id);

    /**
     * @description: 更新用户信息
     * @param: [com.healthyrecipes.pojo.entity.User]
     * @return: void
     */
    void updateMessage(User user);

    /*
     * @description: 获得用户列表
     * @param:
     * @return: List<User>
     */
    List<User> getUserList();

    /**
     * @description: 通过id删除用户
     * @param: [java.lang.Long]
     * @return: void
     */
    void deleteUserById(Integer id);


    /**
     * @description: 管理员登录
     * @param: [com.healthyrecipes.pojo.entity.Admin]
     * @return: com.healthyrecipes.pojo.entity.Admin
     */
    Admin adminLogin(Admin admin);


    /**
     * @description: 对菜品评论
     * @param: [com.healthyrecipes.pojo.entity.Comment]
     * @return: void
     */
    Integer addAComment(Comment comment);

    /**
     * @description: 删除一条评论
     * @param: [java.lang.Integer]
     * @return: void
     */
    void deleteACommentById(Integer id);

    /**
     * @description: 获得对应dish下的评论
     * @param: [java.lang.Integer]
     * @return: java.util.List<com.healthyrecipes.pojo.vo.CommentVO>
     */
    List<CommentVO> getCommentListById(Integer id,Integer userid);

    /**
     * @description: 通过评论Id获得评论内容详情
     * @param: [java.lang.Integer]
     * @return: com.healthyrecipes.pojo.vo.CommentVO
     */
    CommentVO getCommentById(Integer id,Integer userid) throws NoSuchCommentException;

    /**
     * @description: 用户注销
     * @param: [java.lang.Integer]
     * @return: void
     */
    void logout(Integer id);

    /**
     * @description: 做点赞或者取消点赞的功能
     * @param: []
     * @return: void
     */
    void doLike(Integer userId,Integer commentId);

    /**
     * @description: 通过email获得用户的信息
     * @param: [java.lang.String]
     * @return: com.healthyrecipes.pojo.entity.User
     */
    User getUserMessageByEmail(String email);

    /**
     * @description: 朝XingHuo发送信息
     * @param: [java.lang.String]
     * @return: java.lang.String
     */
    String sendMessageToXingHuo(String question);
}
