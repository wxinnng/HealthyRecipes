package com.healthyrecipes.controller;

import com.healthyrecipes.common.constant.MessageConstant;
import com.healthyrecipes.common.properties.JwtProperties;
import com.healthyrecipes.common.result.ResultJson;
import com.healthyrecipes.common.utils.AliOssUtil;
import com.healthyrecipes.common.utils.JwtUtil;
import com.healthyrecipes.common.utils.RedisUtil;
import com.healthyrecipes.exception.LoginException;
import com.healthyrecipes.pojo.entity.*;
import com.healthyrecipes.service.AdminService;
import com.healthyrecipes.service.FoodService;
import com.healthyrecipes.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author:86198
 * @DATE:2024/2/2 14:58
 * @DESCRIPTION: 管理员接口
 * @VERSION:1.0
 */
@RestController
@Slf4j
@Api("管理员接口")
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AliOssUtil aliOssUtil;


    @Autowired
    private FoodService foodService;

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private AdminService adminService;

    /**
     * @description: 获得用户列表
     * @param: []
     * @return: com.healthyrecipes.common.result.ResultJson<java.util.List<com.healthyrecipes.pojo.entity.User>>
     */
    @GetMapping("/users")
    @ApiOperation("获得用户列表")
    public ResultJson<List<User>> getUserList(){
        log.info("admin获得用户列表");
        ResultJson<List<User>> result = new ResultJson<>();
        result.setData(userService.getUserList());
        return result;
    }

    /**
     * @description: 删除对应用户
     * @param: [java.lang.Long]
     * @return: com.healthyrecipes.common.result.ResultJson<java.lang.String>
     */
    @GetMapping("/delete/{id}")
    @ApiOperation("删除对应用户")
    public ResultJson<String> deleteUserByUserId(@PathVariable Integer id){
        log.info("删除id为：{}的用户",id);
        if(id == null)
            return ResultJson.error("id为空");
        try {
            userService.deleteUserById(id);
            return ResultJson.success("删除成功!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResultJson.error("删除失败！");
        }
    }

    /**
     * @description: 重置对应用户密码
     * @param: [java.lang.Long]
     * @return: com.healthyrecipes.common.result.ResultJson<java.lang.String>
     */
    @GetMapping("/reset/{id}")
    @ApiOperation("重置对应用户密码")
    public ResultJson<String> resetPassword(@PathVariable Integer id){
        log.info("重置id为：{}的密码",id);
        if(id == null)
            return ResultJson.error("id为空！");
        try{
            User user = new User();
            user.setId(id);
            user.setPassword(MessageConstant.DEFAULT_PASSWORD);
            userService.updateMessage(user);
            return ResultJson.success("重置成功！");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResultJson.error("重置失败！");
        }
    }

    /**
     * @description: 修改用户状态
     * @param: [com.healthyrecipes.pojo.entity.User]
     * @return: com.healthyrecipes.common.result.ResultJson<java.lang.String>
     */
    @PostMapping("/status")
    @ApiOperation("修改用户状态")
    public ResultJson<String> modifyStatus(@RequestBody User user){
        if(user.getStatus() == null || user.getId()== null)
            return ResultJson.error("用户信息为空！");
        log.info("更新状态的用户{},状态改为{}",user.getId(),user.getStatus());
        try{
            userService.updateMessage(user);
            return ResultJson.success("状态更新成功！");
        }catch(Exception e){
            System.out.println(e.getMessage());
            return ResultJson.error("修改状态失败!");
        }
    }


    /**
     * @description: 管理员登录
     * @param: [com.healthyrecipes.pojo.entity.Admin]
     * @return: com.healthyrecipes.common.result.ResultJson<java.lang.String>
     */
    @PostMapping("/login")
    @ApiOperation("管理员登录")
    public ResultJson<Admin> login(@RequestBody Admin admin){
        if(admin.getUsername() == null || admin.getPassword() == null)
            return ResultJson.error("信息为空");
        log.info("管理员登录{}",admin.getUsername());
        try{
            Admin result = userService.adminLogin(admin);
            if(result == null)
                throw new LoginException("账号或密码错误!");

            //登录成功后，生成jwt令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("admin", result.getId());
            String token = JwtUtil.createJWT(
                    jwtProperties.getSecretKey(),
                    jwtProperties.getTtl(),
                    claims);

            result.setToken(token);     //设置token

            redisUtil.setex("admin:"+result.getId(),token,7200L);//将token放到redis中去。

            return ResultJson.success(result);
        }catch (LoginException e) {
            System.out.println(e.getMessage());
            return ResultJson.error(null);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResultJson.error("服务器异常！");
        }
    }

    /**
     * @description: 修改菜品种类
     * @param: [java.lang.Integer, com.healthyrecipes.pojo.entity.Category]
     * @return: com.healthyrecipes.common.result.ResultJson<java.lang.String>
     */
    @PostMapping("/modifycategory")
    @ApiOperation("修改菜品种类")
    public ResultJson<String> modifyCategory(@RequestBody Category category){
        log.info("修改category {}",category.getId());

        if(category.getId() == null || category.getTitle() == null)
            return ResultJson.error("修改信息不可为空！");

        try{
            foodService.modifyCategory(category);
            return ResultJson.success("修改成功！");
        }catch(Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("修改失败，请稍后再试！");
        }
    }

    /**
     * @description: 添加种类
     * @param: [com.healthyrecipes.pojo.entity.Category]
     * @return: com.healthyrecipes.common.result.ResultJson<java.lang.String>
     */
    @PostMapping("/addcategory")
    @ApiOperation("添加种类")
    public ResultJson<String> addCategory(@RequestBody Category category){
        log.info("添加食品种类");
        if (category == null) return ResultJson.error("添加信息不可为空！");
        try{
            foodService.addCategory(category);
            return ResultJson.success("添加成功！");
        }catch (Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("添加失败!");
        }
    }

    @GetMapping("/deletecategory/{id}")
    @ApiOperation("通过id删除对应的食品种类")
    public ResultJson<String> deleteCategory(@PathVariable Integer id){
        if(id == null) return ResultJson.error("删除的种类不可为空！");
        log.info("删除食品种类 {} ",id);
        try{
            foodService.deleteCategoryById(id);
            return ResultJson.success("删除成功！");
        }catch (Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("删除失败！");
        }
    }

    /**
     * @description: 获得数据统计信息
     * @param: []
     * @return: com.healthyrecipes.common.result.ResultJson<com.healthyrecipes.pojo.entity.Data>
     */
    @GetMapping("/data")
    @ApiOperation("获得统计数据")
    public ResultJson<Data> messageData(){
        String nowDate = redisUtil.nowDate();
        log.info("信息统计{}",nowDate);
        Data result = new Data();
        try{
            /*填充信息*/
            result.setFd(adminService.fullData());
            result.setUserOnline(redisUtil.scard(nowDate));                               // 设置今天截止到现在，上线过的用户数量
            result.setRegisterNum(Long.valueOf( redisUtil.get("msg:registerNum").toString()));// 注册的用户数量
            result.setApiUse(Long.valueOf(redisUtil.get("msg:apiUsed").toString()));          // 服务器的api已被调用的次数

            /*拿到userLine的信息*/
            Map<Object, Object> userLine = redisUtil.hgetall("msg:userLine");
            ArrayList<String> timeLine = new ArrayList<>();
            ArrayList<Integer> userLineNum = new ArrayList<>();

            //填充timeLine
            for (Object o : userLine.keySet()) {
                timeLine.add(o.toString());
            }
            result.setTimeLine(timeLine);

            //填充userLine
            for (Object value : userLine.values()) {
                userLineNum.add(Integer.valueOf(value.toString()));
            }
            result.setUserLine(userLineNum);
            return ResultJson.success(result);
        }catch(Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("信息获取失误");
        }
    }


    @PostMapping("/modifyimg/{id}")
    @ApiOperation("修改图片")
    public ResultJson<String> updateFoodImage(@RequestBody MultipartFile file, @PathVariable Integer id){
        log.info("修改图片");
        try{
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            log.info("上传的文件名 {}",originalFilename);

            //截取原始文件名的后缀
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新文件名称
            String objectName = UUID.randomUUID().toString() + extension;
            //文件的请求路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            foodService.updateFood(new Food(id,null,null,filePath,null,null,null,null,null));
            return ResultJson.success("图片上传成功！");
        }catch (Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("图片上传失败！");
        }
    }

    /**
     * @description: 修改食品信息
     * @param: []
     * @return: com.healthyrecipes.common.result.ResultJson<java.lang.String>
     */
    @PostMapping("/modifymsg/{id}")
    @ApiOperation("修改菜品信息")
    public ResultJson<String> modifymsg(@RequestBody Food food){
        log.info("修改食品信息");
        try{
            foodService.updateFood(food);
            return ResultJson.success("修改成功！");
        }catch(Exception e ){
            System.err.println(e.getMessage());
            return ResultJson.error("服务器异常！");
        }
    }

    @PostMapping("/insert")
    @ApiOperation("插入一条数据")
    public ResultJson<String> insertAFood(@RequestBody Food food){
        log.info("插入一条数据{}",food);
        try{
            foodService.insertAFood(food);
            return ResultJson.success("添加成功！");
        }catch(Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("插入失败！");
        }
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("删除一条食品数据")
    public ResultJson<String> deleteAFood(@PathVariable Integer id){
        log.info("删除一条数据");
        try{
            foodService.deleteFood(id);
            return ResultJson.success("删除成功！");
        }catch (Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("删除失败！");
        }
    }

}
