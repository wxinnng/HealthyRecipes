package com.healthyrecipes.controller;

import com.healthyrecipes.common.constant.MessageConstant;
import com.healthyrecipes.common.properties.JwtProperties;
import com.healthyrecipes.common.result.ResultJson;
import com.healthyrecipes.exception.NoSuchCommentException;
import com.healthyrecipes.pojo.dto.UserDTO;
import com.healthyrecipes.pojo.entity.CalorieResult;
import com.healthyrecipes.pojo.entity.Comment;
import com.healthyrecipes.pojo.entity.User;
import com.healthyrecipes.common.utils.JwtUtil;
import com.healthyrecipes.common.utils.RedisUtil;
import com.healthyrecipes.pojo.vo.*;
import com.healthyrecipes.service.FoodService;
import com.healthyrecipes.service.UserService;
import com.mysql.jdbc.Constants;
import com.mysql.jdbc.util.ResultSetUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.ReusableMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * @Author:86198
 * @DATE:2023/12/30 14:32
 * @DESCRIPTION: 用户相关接口
 * @VERSION:1.0
 */
@RestController
@Slf4j
@RequestMapping("/user")
@Api("用户相关接口")
public class UserController {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private JavaMailSender mailSender;

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private FoodService foodService;


    /**
     * @description: 用户登录接口
     * @param: [com.healthyrecipes.pojo.vo.UserVO]
     * @return: com.healthyrecipes.common.result.ResultJson<com.healthyrecipes.pojo.vo.LoginVO>
     */
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public ResultJson<LoginVO>login(@RequestBody UserVO user){
        log.info("用户登录 {}" ,user);
        
        String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        log.info("登录：用户提交的密码（加密后）: {}",password);

        User targetUser = userService.login(user.getEmail(), password);

        if(targetUser == null){
            log.info("邮箱或密码错误！");
            return ResultJson.error("邮箱或密码错误！");
        }
        String token = null;
        if(redisUtil.exists("token:"+user.getEmail()))
        {
            token = (String) redisUtil.get("token:"+user.getEmail());
        }else{
            //登录成功后，生成jwt令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("user", user.getEmail());
            token = JwtUtil.createJWT(
                    jwtProperties.getSecretKey(),
                    jwtProperties.getTtl(),
                    claims);
            //将token放到redis中去。
            redisUtil.setex("token:"+user.getEmail(),token,3600 * 24 * 30);

        }
        String nowData = redisUtil.nowDate();
        //将登录的useID放到对应日期的集合中去
        redisUtil.sadd("time:"+nowData,targetUser.getId());

        

        //过期时间一天
        redisUtil.expire(nowData, 3600 * 24);


        if (! redisUtil.sismember("time:"+nowData,targetUser.getId())) {

            if(!redisUtil.hexists("msg:userLine",nowData))
                redisUtil.hset("msg:userLine",nowData,1);
            else
                redisUtil.hincrby("msg:userLine", nowData,1);
        }
        //将最终的结果返回
        return ResultJson.success(new LoginVO(token, targetUser));
    }


    /**
     * @description: 用户注册接口
     * @param: [java.lang.String]
     * @return: com.healthyrecipes.common.result.ResultJson<java.lang.String>
     */
    @GetMapping("/getCode")
    @ApiOperation("获得验证码")
    public ResultJson<String> getRegisterCode(@RequestParam String email) throws Exception{
        log.info("邮箱：{}",email);

        /*邮箱是否可注册*/
        if(!userService.isEmailUsed(email)){
            log.info("邮箱已经被注册：{}",email);
            return ResultJson.error("邮箱已经被注册！");
        }

        /*发送验证码*/
        SimpleMailMessage message = new SimpleMailMessage();
        Random random = new Random();
        String code = String.valueOf(random.nextInt(899999) + 10000);
        message.setFrom("19846811030@163.com");
        message.setTo(email);
        message.setSubject("健康食谱注册验证:");
        message.setText("邮箱验证码为: " + code +" ,请勿发送给他人,两分钟内有效！");
        try {
            mailSender.send(message);                                      //发送邮箱
            redisUtil.setex(email+":code",code,120);            //保存到Redis中
            log.info("验证码邮件已发送。");
            return ResultJson.success("发送成功");                             //
        } catch (Exception e) {
            System.err.println("发送验证码邮件时发生异常了！");
            return ResultJson.error("验证码发送失误，请稍后再试！");
        }
    }

    /**
     * @description: 注册
     * @param: [com.healthyrecipes.pojo.vo.UserVO]
     * @return: com.healthyrecipes.common.result.ResultJson<java.lang.String>
     */
    @PostMapping("/register")
    @ApiOperation("注册")
    public ResultJson<String> register(@RequestBody UserVO userVO){

        userVO.setUsername("用户"+userVO.getCode()+"nex");

        log.info("用户注册：{}", userVO);
        //从redis中获得code
        String code = (String) redisUtil.get(userVO.getEmail()+":code");

        if(code == null){
            return ResultJson.error("验证错误，请稍后再试！");
        }else if (!code.equals(userVO.getCode())){
            return ResultJson.error("验证错误，请稍后再试！");
        }

        //Md5加密
        String password = DigestUtils.md5DigestAsHex(userVO.getPassword().getBytes());

        log.info("注册: 用户提交的密码（加密后） {}",password);

        //封装信息
        UserDTO registerDTO = new UserDTO(userVO.getUsername(), userVO.getEmail(), password);

        //Service层处理
        Integer newUserId = userService.register(registerDTO);

        //注册的用户数++
        redisUtil.incrby("msg:registerNum",1L);

        //添加评论容器
        redisUtil.sadd(MessageConstant.USER_COMMENT_KEY+":"+newUserId,-1);

        //最后返回
        return ResultJson.success("注册成功！");
    }

    @GetMapping("/logout/{id}")
    public ResultJson<String> logout(@PathVariable Integer id){
        log.info("用户注销 {}",id);
        try{
            userService.logout(id);
            return ResultJson.success("注销成功！");
        }catch (Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("操作失败！");
        }
    }

    /**
     * @description: 更新用户信息
     * @param: [com.healthyrecipes.pojo.entity.User]
     * @return: com.healthyrecipes.common.result.ResultJson<java.lang.String>
     */
    @PostMapping("/updatemsg")
    @ApiOperation("更新用户信息")
    public ResultJson<String> updateMessage(@RequestBody User user){
        log.info("更新用户信息: id={}",user);
        try{
            userService.updateMessage(user);
            return ResultJson.success("更新成功");
        }catch(Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("更新失败！");
        }
    }

    /**
     * @description: 获得用户信息
     * @param: [java.lang.Long]
     * @return: com.healthyrecipes.common.result.ResultJson<com.healthyrecipes.pojo.entity.User>
     */
    @GetMapping("/msg/{id}")
    @ApiOperation("获得用户信息")
    public ResultJson<UserFoodVO> getUserMessage(@PathVariable Integer id){
        log.info("查询用户信息: id={}",id);
        try{
            UserFoodVO userFoodVO = new UserFoodVO();
            userFoodVO.setUser(userService.getUserMessageById(id));
            userFoodVO.setTarget(foodService.getRecommendedIntake(id));
            return ResultJson.success(userFoodVO);
        }catch(Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("服务器异常");
        }
    }


    /**
     * @description: 用户对菜品发表评论。
     * @param: [com.healthyrecipes.pojo.entity.Comment]
     * @return: com.healthyrecipes.common.result.ResultJson<java.lang.String>
     */
    @ApiOperation("用户发表评论")
    @PostMapping("/addcomment")
    public ResultJson<String> addAComment(@RequestBody Comment comment){
        log.info("用户{}对Dish{}发表评论",comment.getUserId(),comment.getDishId());

        try{
            Integer commentId = userService.addAComment(comment);
            log.info("{}",commentId);
            redisUtil.sadd(MessageConstant.COMMENT_KEY+":"+commentId,-1);
            return ResultJson.success("评论成功");
        }catch(Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("评论失败!");
        }
    }

    /**
     * @description: 删除一条评论
     * @param: [java.lang.Integer]
     * @return: com.healthyrecipes.common.result.ResultJson<java.lang.String>
     */
    @ApiOperation("删除评论")
    @GetMapping("/rmcomment/{id}")
    public ResultJson<String> deleteAComment(@PathVariable Integer id){
        log.info("删除评论{}",id);
        try{
            userService.deleteACommentById(id);
            redisUtil.del(MessageConstant.COMMENT_KEY+":"+id);   //删除redis中的key
            return ResultJson.success("删除成功！");
        }catch (Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("评论删除失败！");
        }
    }

    /**
     * @description: 获得评论列表
     * @param: [java.lang.Integer]
     * @return: com.healthyrecipes.common.result.ResultJson<java.util.List<com.healthyrecipes.pojo.vo.CommentVO>>
     */
    @ApiOperation("获得评论列表")
    @GetMapping("/comments/{id}/{userid}")
    public ResultJson<List<CommentVO>> getCommentListByDishId(@PathVariable Integer id,@PathVariable Integer userid){
        log.info("获得对应dish_id评论 {}",id);
        try{
            return ResultJson.success(userService.getCommentListById(id,userid));
        }catch (Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("服务器异常！");
        }
    }

    @GetMapping("/getcomment/{id}/{userid}")
    @ApiOperation("通过评论id获得评论内容")
    public ResultJson<CommentVO> getCommentById(@PathVariable Integer id,@PathVariable Integer userid){
        try{
            return ResultJson.success(userService.getCommentById(id,userid));
        }catch(NoSuchCommentException e){
            System.out.println(e.getMessage());
            return ResultJson.error("没有该评论！");
        } catch (Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("服务器异常！");
        }
    }

    /**
     * @description: 点赞或者取消
     * @param: [java.lang.Integer, java.lang.Integer]
     * @return: com.healthyrecipes.common.result.ResultJson<java.lang.String>
     */
    @PostMapping("/dolike")
    @ApiOperation("点赞或者取消点赞")
    public ResultJson<String> doLike(@RequestParam Integer userid,@RequestParam Integer commentId) {
        log.info("点赞 {} -> {}", userid, commentId);
        //在发表评论的时候，关于评论的点赞集合就已经创建了
        userService.doLike(userid,commentId);
        return ResultJson.success("操作成功！");
    }
    /**
     * 找回密码时，点击获取验证码
     * @param email
     * @return
     * @throws Exception
     */
    @GetMapping("/findBack")
    @ApiOperation("找回密码")
    public ResultJson<String> findBack(@RequestParam String email ,
                                       @RequestParam String password ,
                                       @RequestParam String code){
        //通过邮箱查询用户
        User user = userService.getUserMessageByEmail(email);

        //从redis中获得正确的code
        String right = (String) redisUtil.get(email+":code");

        //比对验证码
        if(code == null){
            return ResultJson.error("验证错误，请稍后再试！");
        }else if (!code.equals(right)){
            return ResultJson.error("验证错误，请稍后再试！");
        }

        //将修改后的密码进行Md5加密
        String password2 = DigestUtils.md5DigestAsHex(password.getBytes());
        log.info("注册: 用户提交的密码（加密后） {}",password);

        //修改获取的用户密码
        user.setPassword(password2);
        //修改数据库用户
        userService.updateMessage(user);

        return ResultJson.success("修改成功");
    }

}
