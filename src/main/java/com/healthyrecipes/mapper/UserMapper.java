package com.healthyrecipes.mapper;


import com.healthyrecipes.pojo.dto.CommentDTO;
import com.healthyrecipes.pojo.dto.UserDTO;
import com.healthyrecipes.pojo.entity.Comment;
import com.healthyrecipes.pojo.entity.User;
import com.healthyrecipes.pojo.vo.CommentVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author:86198
 * @DATE:2024/1/21 15:35
 * @DESCRIPTION:
 * @VERSION:1.0
 */
@Mapper
public interface UserMapper {

    /**
     * @description: 通过邮箱查询用户
     * @param: [java.lang.String]
     * @return: com.healthyrecipes.pojo.entity.User
     */

    User getUserByEmail(String email,String password);

    /**
     * @description: 保存用户信息
     * @param: [com.healthyrecipes.pojo.dto.UserDTO]
     * @return: void
     */

    Integer save(UserDTO user);


    /**
     * @description: 查询用户信息
     * @param: [java.lang.Long]
     * @return: com.healthyrecipes.pojo.entity.User
     */
    @Select("select id,username,weight,phone,height,avatar,sex,birth,exercise,target from user where id = #{id}")
    User getUserById(Integer id);

    /**
     * @description: 更新用户信息
     * @param: [com.healthyrecipes.pojo.entity.User]
     * @return: void
     */
    void updateMessage(User user);

    /**
     * @description: 获得用户列表
     * @param: []
     * @return: java.util.List<com.healthyrecipes.pojo.entity.User>
     */
    @Select("select * from user")
    List<User> getUserList();

    /**
     * @description: 通过id删除用户
     * @param: [java.lang.Long]
     * @return: void
     */
    @Delete("delete from user where id=#{id}")
    void deleteById(Integer id);


    /**
     * @description: 添加一个评论
     * @param: [com.healthyrecipes.pojo.entity.Comment]
     * @return: void
     */
    Integer addAComment(Comment comment);

    /**
     * @description: 删除一条评论，以及子评论
     * @param: [java.lang.Integer]
     * @return: void
     */
    @Delete("delete from comment where id =#{id} or parent_id=#{id}")
    void deleteCommentById(Integer id);

    /**
     * @description: 获得对应DishId下面的父级评论
     * @param: [java.lang.Integer]
     * @return: java.util.List<com.healthyrecipes.pojo.vo.CommentVO>
     */
    List<CommentDTO> getFatherCommentListByDishId(Integer id);

    /**
     * @description: 获得对应Dish下的子级评论
     * @param: [java.lang.Integer]
     * @return: java.util.List<com.healthyrecipes.pojo.dto.CommentDTO>
     */
    List<CommentDTO> getChildrenCommentListByDishId(Integer id);




    /**
     * @description: 获得对应parent_id下的child
     * @param: [com.healthyrecipes.pojo.dto.CommentSearchDTO]
     * @return: java.util.List<com.healthyrecipes.pojo.dto.CommentDTO>
     */
    List<CommentDTO> getChildrenCommentById(Integer parentId);


    /**
     * @description: 通过commentId获得评论的详细内容
     * @param: [java.lang.Integer]
     * @return: com.healthyrecipes.pojo.vo.CommentVO
     */
    CommentVO getFatherCommentById(Integer id);



    /**
     * @description: 进行对点赞的数据内容更新
     * @param: [java.lang.Integer, java.lang.Integer]
     * @return: void
     */
    void operateForLike(Integer commentId,Integer operator);

}
