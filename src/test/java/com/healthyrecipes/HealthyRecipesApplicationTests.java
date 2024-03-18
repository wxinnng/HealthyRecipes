package com.healthyrecipes;

import com.healthyrecipes.common.utils.RedisUtil;
import com.healthyrecipes.mapper.FoodMapper;
import com.healthyrecipes.mapper.UserMapper;
import com.healthyrecipes.pojo.dto.UserDTO;
import com.healthyrecipes.pojo.entity.Category;
import com.healthyrecipes.pojo.entity.Comment;
import com.healthyrecipes.pojo.entity.Food;
import com.healthyrecipes.pojo.entity.User;
import com.healthyrecipes.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;


@SpringBootTest
class HealthyRecipesApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private UserMapper userMapper;



    @Test
    void testGetByEmail(){
        String email = "1320444219@qq.com";
        System.out.println(userMapper.getUserByEmail(email,null));
    }

    @Test
    void test(){
        System.out.println(DigestUtils.md5DigestAsHex("HealthyRecipes123".getBytes()));
    }

    @Test
    void testGet(){
        System.out.println(userMapper.getUserById(1));
    }

    @Test
    void testUpdate(){
        User user = new User();
        user.setId(1);

        user.setBirth(LocalDate.now());
        userMapper.updateMessage(user);
    }

    @Autowired
    private FoodMapper foodMapper;

    @Test
    void testGetCategoryList(){
        System.out.println(foodMapper.getCategoryList(new Category()));
    }

    @Test
    void testSaveList(){
        ArrayList<Integer> list = new ArrayList<>();
        list.add(4141);
        list.add(4142);
        list.add(4144);
        System.out.println(foodMapper.getFoodListByIds(list));
    }

    @Resource
    private RedisUtil redisUtil;

    @Test
    public void testLlen(){
        System.out.println(redisUtil.llen("1:1"));
    }


    @Test
    void testDeleteComment(){
        userMapper.deleteCommentById(1);
    }

    @Autowired
    private UserService userService;



    @Test
    void testFoodUpdate() {
        foodMapper.updateFood(new Food(4141, null, "米饭，又叫米、大米饭、饭、蒸米、锅巴饭、煮", null, null, null, null, null, null));
    }
}
