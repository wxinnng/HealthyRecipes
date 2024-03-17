package com.healthyrecipes.controller;

import com.aliyun.broadscope.bailian.sdk.ApplicationClient;
import com.aliyun.broadscope.bailian.sdk.models.*;
import com.healthyrecipes.common.properties.BaiLianProperties;
import com.healthyrecipes.common.result.ResultJson;
import com.healthyrecipes.common.utils.AliOssUtil;
import com.healthyrecipes.common.utils.RedisUtil;
import com.healthyrecipes.config.BaiLianConfiguration;
import com.healthyrecipes.exception.AIUseFailException;
import com.healthyrecipes.pojo.entity.AskContent;
import com.healthyrecipes.pojo.entity.User;


import com.healthyrecipes.service.FoodService;
import com.healthyrecipes.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;


    @Autowired
    private ApplicationClient client;

    @Autowired
    private BaiLianProperties properties;

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private UserService userService;



    /**
     * 文件上传
     * @param avatar   id 传 -1代表食物图片， 。。。
     * @return
     */
    //TODO:如果BaseController中上传图片的可行的话，就修改这里
    @PostMapping("/upload/{id}")
    @ApiOperation("头像上传")
    public ResultJson<String> upload(@RequestBody MultipartFile avatar, @PathVariable Integer id){
        log.info("头像上传：{}",id);

        try {
            //原始文件名
            String originalFilename = avatar.getOriginalFilename();
            log.info("上传的文件名 {}",originalFilename);

            //截取原始文件名的后缀
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新文件名称
            String objectName = UUID.randomUUID().toString() + extension;
            //文件的请求路径
            String filePath = aliOssUtil.upload(avatar.getBytes(), objectName);
            userService.updateMessage(new User(id,filePath));

            return ResultJson.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
        }
        return ResultJson.error("上传失败");
    }



    /**
     * @description: 使用AI问答
     * @param: []
     * @return: com.healthyrecipes.common.result.ResultJson<java.lang.String>
     */
    @PostMapping("/ai")
    @ApiOperation("使用ai问答")
    public ResultJson<String> useAI(@RequestBody AskContent askContent){
        log.info("使用AI问答");
        try{
            String key = "ai:"+askContent.getUserid();
            if(redisUtil.exists(key))
                redisUtil.incrby(key,1L);
            else
                redisUtil.set(key,1);
            /*问题封装 */
            ArrayList<ChatRequestMessage> message = new ArrayList<>();
            message.add(new ChatSystemMessage("你是一个营养健康师，帮助回答各类关于饮食健康的问题"));
            message.add(new ChatUserMessage(askContent.getQuestion()));

            //请求对象
            CompletionsRequest request = new CompletionsRequest().setAppId(properties.getApp_id())
                    .setMessages(message)
                    .setParameters(new CompletionsRequest.Parameter().setResultFormat("message"));

            CompletionsResponse response = client.completions(request);

            if(!response.isSuccess()){
                System.out.printf("failed to create completion, requestId: %s, code: %s, message: %s\n",
                        response.getRequestId(), response.getCode(), response.getMessage());
                throw new AIUseFailException("AI使用错误！");
            }

            //调用成功，返回结果。
            return ResultJson.success(response.getData().getChoices().get(0).getMessage().getContent());

        }catch (AIUseFailException e){
            System.err.println(e.getLocalizedMessage() + e.getMessage());
            return ResultJson.error("AI机器人出了点错误，请稍后再试 ~");
        } catch (Exception e){
            System.err.println(e.getLocalizedMessage() + e.getMessage());
            return ResultJson.error("服务器异常!");
        }
    }
}
