package com.healthyrecipes.service.Impl;

import com.alibaba.fastjson.JSONObject;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.healthyrecipes.common.constant.MessageConstant;
import com.healthyrecipes.common.properties.BaiduAIProperties;
import com.healthyrecipes.common.utils.Base64Util;
import com.healthyrecipes.common.utils.HttpUtil;
import com.healthyrecipes.common.utils.RedisUtil;
import com.healthyrecipes.mapper.DishMapper;
import com.healthyrecipes.pojo.dto.DishDTO;
import com.healthyrecipes.pojo.entity.BaiduAIToken;
import com.healthyrecipes.pojo.entity.BaiduDish;
import com.healthyrecipes.pojo.entity.Dish;
import com.healthyrecipes.pojo.entity.Food;
import com.healthyrecipes.pojo.vo.DishPageVO;
import com.healthyrecipes.pojo.vo.RandDishVO;
import com.healthyrecipes.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


/**
 * @Author:86198
 * @DATE:2024/1/27 20:11
 * @DESCRIPTION: 有关菜品的Service
 * @VERSION:1.0
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DishMapper dishMapper;


    @Autowired
    private BaiduAIProperties baiduAIProperties;

    /**
     * @description: 获得百度AI的access_token
     * @param: [java.lang.String, java.lang.String, java.lang.String]
     * @return: java.lang.String
     */
    private String getBaiduAIToken(){
        /*拿到配置信息*/
        /*String appId = MessageConstant.APP_ID;
        String ak = MessageConstant.AK;
        String sk = MessageConstant.SK;*/
        String appId = baiduAIProperties.getAppId();
        String ak = baiduAIProperties.getAk();
        String sk = baiduAIProperties.getSk();

        /*首先从Redis中查找，如果有的话，就直接取，否则重新获取*/
        String token = (String) redisUtil.get(appId);
        if(token != null)   // token存在，直接返回
            return token;

        /*不存在的话，就要重新获取，并且放到redis中去*/
        String path = MessageConstant.BAIDU_TOKEN_URL
                + "?grant_type=client_credentials&client_id="+ak+"&client_secret="+sk; //请求路径

        /*封装请求头*/
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept","application/json");
        HttpEntity httpEntity = new HttpEntity(null, headers);

        try{
            /*发送请求，获得access_token，并且放到Redis中去*/
            BaiduAIToken result = restTemplate.postForObject(path, httpEntity, BaiduAIToken.class);
            redisUtil.setex(appId,result.getAccess_token(),60 * 60 * 24 * 30); //过期时间是30天
            return result.getAccess_token();
        }catch (Exception e){
            log.info("百度AI识物token获取失败!");
            return null;
        }
    }


    /**
     * @description: 解析图片
     * @param: [org.springframework.web.multipart.MultipartFile]
     * @return: com.healthyrecipes.pojo.entity.BaiduDish
     */
    @Override
    public BaiduDish parseImage(MultipartFile image) {

        String access_token = getBaiduAIToken(); //首先先获得token

        if(access_token == null) //access_token 获得失败，直接返回
            return null;
        try{
            /*获得图片对应的Base64编码的字符串*/
            String imageBase64 = Base64Util.encode(convertToByteArray(image));
            String imgParam = URLEncoder.encode(imageBase64, StandardCharsets.UTF_8);

            /*拼装路径、请求参数、发送请求*/
            String param = "image="+imgParam+"&top_num="+5;
            String result = HttpUtil.post(MessageConstant.BAIDU_AI_URL,access_token,param);

            /*返回结果*/
            return JSONObject.parseObject(result, BaiduDish.class);
        }catch(Exception e){
            log.info("图片识别失败");
            return null;
        }
    }

    @Override
    public List<RandDishVO> getThreeRandDishes() {
        /*随机获得10个整数作为要查的foodid*/
        ArrayList<Integer> ids = new ArrayList<>();

        for(int i = 0; i < 3 ;i ++) {
             ids.add(ThreadLocalRandom.current().nextInt(1, 100));
        }
        return dishMapper.getDishByIdsNoDetail(ids);
    }

    @Override
    public DishPageVO getDishList(DishDTO dishDTO) {
        DishPageVO dishPageVO = new DishPageVO();

        //决定要不要分页查询
        if(dishDTO.getPageNum() != null && dishDTO.getPageSize() != null)
            PageHelper.startPage(dishDTO.getPageNum(),dishDTO.getPageSize());

        //从数据库中获取数据
        Page<Dish> dishes = dishMapper.getDish(dishDTO.getId());

        //封装数据
        dishPageVO.setDishes(dishes);
        dishPageVO.setNum(100);

        //返回
        return dishPageVO;
    }

    private byte[] convertToByteArray(MultipartFile file) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            return bytes;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
