package com.healthyrecipes.service.Impl;

import com.healthyrecipes.common.constant.MessageConstant;
import com.healthyrecipes.common.utils.RedisUtil;
import com.healthyrecipes.exception.NoSuchCommentException;
import com.healthyrecipes.mapper.AdminMapper;
import com.healthyrecipes.mapper.UserMapper;
import com.healthyrecipes.pojo.dto.CommentDTO;
import com.healthyrecipes.pojo.dto.UserDTO;
import com.healthyrecipes.pojo.entity.*;
import com.healthyrecipes.pojo.vo.CommentVO;
import com.healthyrecipes.service.UserService;
import com.healthyrecipes.websocket.SparkConsoleListener;
import io.github.briqt.spark4j.SparkClient;
import io.github.briqt.spark4j.constant.SparkApiVersion;
import io.github.briqt.spark4j.model.SparkMessage;
import io.github.briqt.spark4j.model.request.SparkRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author:86198
 * @DATE:2024/1/21 15:36
 * @DESCRIPTION:
 * @VERSION:1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private UserMapper userMapper;

    @Resource
    private SparkConsoleListener sparkConsoleListener;

    @Resource
    private SparkClient sparkClient;

    @Autowired
    private AdminMapper adminMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)   //开启事务注解
    public Integer register(UserDTO registerDTO) {
        return userMapper.save(registerDTO);              //保存用户基本信息
    }

    @Override
    public User login(String email, String password) {
        return userMapper.getUserByEmail(email, password);
    }

    @Override
    public User getUserMessageById(Integer id) {
        return userMapper.getUserById(id);
    }

    @Override
    public void updateMessage(User user) {
        if (user == null) {
            return;
        }
        userMapper.updateMessage(user);
    }

    @Override
    public List<User> getUserList() {
        return userMapper.getUserList();
    }

    @Override
    public Admin adminLogin(Admin admin) {
        return adminMapper.getAdmin(admin);
    }




    /**
     * @description: 获得当前到午夜12点的秒数
     * @param: []
     * @return: java.lang.Long
     */
    private Long getSecondsToMidnight() {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 获取明天凌晨12点的时间
        LocalDateTime midnightTomorrow = now.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        // 计算两个时间之间的时长
        Duration duration = Duration.between(now, midnightTomorrow);

        // 将时长转换为秒
        return duration.getSeconds();
    }

    @Override
    public CommentVO getCommentById(Integer id,Integer userid) throws NoSuchCommentException {

        CommentVO father = userMapper.getFatherCommentById(id);

        //用户所有的点赞评论集合
        Set idsComment = (Set)redisUtil.smembers(MessageConstant.USER_COMMENT_KEY + ":" + userid);

        //如果当前评论在集合中，就设置成true
        father.setIsLike(idsComment.contains(id));

        //把子评论也这样搞
        List<CommentDTO> children = userMapper.getChildrenCommentById(id);

        for (CommentDTO child : children) {
            child.setIsLike(idsComment.contains(child.getId()));
        }
        father.setChildren(children);
        return father;
    }

    /**
     * @description: 注销用户
     * @param: [java.lang.Integer]
     * @return: void
     */
    @Override
    public void logout(Integer id) {
        //获得用户邮箱
        String email = userMapper.getUserById(id).getEmail();
        //删除redis中的token
        redisUtil.del("token:"+email);
        //删除数据库中的信息
        userMapper.deleteById(id);
    }

    @Override
    public List<CommentVO> getCommentListById(Integer id,Integer userid) {

        /*获得评论*/
        List<CommentDTO> fatherComment = userMapper.getFatherCommentListByDishId(id);            //父级评论

        Set<Object> idsComment = redisUtil.smembers(MessageConstant.USER_COMMENT_KEY + ":" + userid);

        List<CommentDTO> childrenComment = userMapper.getChildrenCommentListByDishId(id);        //子级评论

        /*判断子级评论中，当前用户是否点赞*/
        for (CommentDTO child : childrenComment) {
            child.setIsLike(idsComment.contains(child.getId()));
        }
        System.out.println(childrenComment);
        //结果集
        List<CommentVO> result = new ArrayList<>();

        //封装结果集
        for (CommentDTO father : fatherComment) {
            CommentVO ele = new CommentVO(father.getId(),
                    father.getUserid(),                   //userid
                    father.getUsername(),                 //用户名
                    father.getAvatar(),                   //头像url
                    father.getContent(),                  //评论内容
                    father.getDate(),                     //点赞的日期
                    father.getLikeNum(),                  //点赞的数量
                    idsComment.contains(father.getId()),  //当前用户是否点赞
                    childrenComment.stream().filter(item -> Objects.equals(item.getParentId(), father.getId()))
                            .collect(Collectors.toList()) //子评论
                    ); //封装一个CommentVO
            result.add(ele);   //放到结果中
        }

        //返回结果
        return result;

    }

    @Override
    public void deleteACommentById(Integer id) {
        userMapper.deleteCommentById(id);
    }


    @Override
    public User getUserMessageByEmail(String email) {
        return userMapper.getUserByEmail(email,null);
    }

    @Override
    public void sendMessageToXingHuo(String question, Session session) {

        List<SparkMessage> messages = new ArrayList<>();   //消息列表
        messages.add(SparkMessage.systemContent(MessageConstant.PRECONDITION));  //预设问题
        messages.add(SparkMessage.userContent(question));  //设置问题

        //发送信息
        SparkRequest sparkRequest = SparkRequest.builder()
                .messages(messages)
                .maxTokens(1024)     //回答的最大token
                .temperature(0.5)    //结果随机性
                .apiVersion(SparkApiVersion.V3_5)   //版本情况
                .build();   //构建

        //重新设置一个session(返回客户端)
        sparkConsoleListener.setSession(session);

        //封装聊天信息
        sparkClient.chatStream(sparkRequest,sparkConsoleListener);
    }

    @Override
    public void doLike(Integer userId,Integer commentId) {

        if(redisUtil.sismember(MessageConstant.COMMENT_KEY+":"+commentId,userId)){
            //redis中存在，就直接删除
            redisUtil.srem(MessageConstant.COMMENT_KEY+":"+commentId,userId);
            redisUtil.srem(MessageConstant.USER_COMMENT_KEY+":"+userId,commentId);
            //更新mysql中评论点赞的数量
            userMapper.operateForLike(commentId,0);  //null是键操作
        }else {
            //redis中不存在就直接添加
            redisUtil.sadd(MessageConstant.COMMENT_KEY+":"+commentId,userId);
            redisUtil.sadd(MessageConstant.USER_COMMENT_KEY+":"+userId,commentId);
            userMapper.operateForLike(commentId,1);  //1是加操作
        }

    }

    @Override
    public Integer addAComment(Comment comment) {
        comment.setDate(LocalDateTime.now());  //加上时间
        userMapper.addAComment(comment);
        return comment.getId();
    }

    @Override
    public void deleteUserById(Integer id) {
        userMapper.deleteById(id);
    }

    @Override
    public boolean isEmailUsed(String email) {
        User isValued = userMapper.getUserByEmail(email, null);
        if (isValued == null)
            //如果从数据库中查到的对象是空，说明可用
            return true;
        else
            //如果对象不为空，但是status是0，说明账号已经注销，也是可以使用的。
            return Objects.equals(isValued.getStatus(), MessageConstant.DISABLE);
    }
}

