package com.healthyrecipes.controller;

import com.github.pagehelper.Page;
import com.healthyrecipes.common.result.ResultJson;
import com.healthyrecipes.exception.BusinessException;
import com.healthyrecipes.pojo.entity.Group;
import com.healthyrecipes.pojo.query.GroupQuery;
import com.healthyrecipes.pojo.vo.GroupUserVO;
import com.healthyrecipes.service.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author:86198
 * @DATE:2024/3/18 18:07
 * @DESCRIPTION: 有关组队的Controller
 * @VERSION:1.0
 */
@Api("组队相关接口")
@RestController
@RequestMapping("/group")
@Slf4j
public class GroupController {

    @Autowired
    private GroupService groupService;

    /**
     * @description: 创建一个组队
     * @param: [com.healthyrecipes.pojo.entity.Group]
     * @return: com.healthyrecipes.common.result.ResultJson<java.lang.String>
     */
    @PostMapping("/create")
    @ApiOperation("创建一个组")
    public ResultJson<String> createGroup(@RequestBody Group group){
        log.info("创建一个组队 {} ",group);
        try{
            groupService.createGroup(group);
            return ResultJson.success("创建成功！");
        }catch(BusinessException e){
            System.err.println(e.getMessage());
            return ResultJson.error(e.getMessage());
        }catch(Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("服务器异常！");
        }
    }

    /**
     * @description: 获得组队列表信息
     * @param: [com.healthyrecipes.pojo.query.GroupQuery]
     * @return: com.healthyrecipes.common.result.ResultJson<com.github.pagehelper.Page<com.healthyrecipes.pojo.entity.Group>>
     */
    @PostMapping("/getlist")
    @ApiOperation("获得组队信息")
    public ResultJson<Page<Group>> getList(@RequestBody GroupQuery query){
        log.info("获得List {}",query);
        try{
            return ResultJson.success(groupService.getList(query));
        }catch(Exception e){
            log.error(e.getMessage());
            return ResultJson.error("服务器异常！");
        }
    }

    /**
     * @description: 获得一个组队的详情
     * @param: [java.lang.Integer]
     * @return: com.healthyrecipes.common.result.ResultJson<com.healthyrecipes.pojo.vo.GroupUserVO>
     */
    @GetMapping("/groupdetail/{id}")
    @ApiOperation("获得组队的详细信息")
    public ResultJson<GroupUserVO> getDetail(@PathVariable Integer id){
        log.info("获得组队的详细信息");
        try{
            return ResultJson.success(groupService.getDetail(id));
        }catch(Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("服务器异常！");
        }
    }

    /**
     * @description: 通过邀请码，进入组队
     * @param: [java.lang.Integer]
     * @return: com.healthyrecipes.common.result.ResultJson<java.lang.String>
     */
    @GetMapping("/join")
    @ApiOperation("通过邀请码，组队")
    public ResultJson<String> Join(@RequestParam Integer userid,@RequestParam String code){
        log.info("用户:{} 邀请码：{}",userid,code);
        try{
            groupService.joinInGroup(userid,code);
            return ResultJson.success("入队成功！");
        }catch(BusinessException e){
            System.err.println(e.getMessage());
            return ResultJson.error(e.getMessage());
        }catch(Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("服务器异常!");
        }
    }

    /**
     * @description: 用户退群、群主解散、群主解散群聊都可以使用这个接口
     * @param: [java.lang.Integer, java.lang.Integer]
     * @return: com.healthyrecipes.common.result.ResultJson<java.lang.String>
     */
    @GetMapping("/delete")
    @ApiOperation("删除一个群成员")
    public ResultJson<String> deleteMember(@RequestParam Integer userid,@RequestParam Integer groupId){
        log.info("{} 群中的成员 {}，被踢出。",userid,groupId);
        try{
            groupService.delete(userid,groupId);
            return ResultJson.success("操作成功！");
        } catch (BusinessException e){
            System.err.println(e.getMessage());
            return ResultJson.error(e.getMessage());
        } catch(Exception e){
            System.err.println(e.getMessage());
            return ResultJson.error("服务器异常！");
        }
    }
}
