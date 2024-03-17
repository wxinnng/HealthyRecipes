package com.healthyrecipes.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.healthyrecipes.common.constant.MessageConstant;
import com.healthyrecipes.common.utils.RedisUtil;
import com.healthyrecipes.exception.CannotDeleteException;
import com.healthyrecipes.mapper.DishMapper;
import com.healthyrecipes.mapper.FoodMapper;
import com.healthyrecipes.mapper.SaveMapper;
import com.healthyrecipes.mapper.UserMapper;
import com.healthyrecipes.pojo.dto.FoodDTO;
import com.healthyrecipes.pojo.dto.MessageDTO;
import com.healthyrecipes.pojo.entity.*;
import com.healthyrecipes.pojo.vo.*;
import com.healthyrecipes.service.FoodService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author:86198
 * @DATE:2024/2/18 19:34
 * @DESCRIPTION: food的service层
 * @VERSION:1.0
 */
@Service
@Slf4j
public class FoodServiceImpl implements FoodService {

    @Autowired
    private FoodMapper foodMapper;

    @Autowired
    private DishMapper dishMapper;

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SaveMapper saveMapper;
    @Override
    public PageVO getFoodList(FoodDTO foodDTO) {

        PageVO pageVO = new PageVO();

        if(foodDTO.getPageNum() != null && foodDTO.getPageSize() != null)
            PageHelper.startPage(foodDTO.getPageNum(),foodDTO.getPageSize());

        Page<Food> foodList = foodMapper.getFoodList(foodDTO);

        pageVO.setFoods(foodList);
        if (foodDTO.getCategory_id() == null)
            pageVO.setNum(690);
        else
            pageVO.setNum(foodMapper.getFoodNumById(foodDTO.getCategory_id()));
        return pageVO;
    }

    @Override
    public List<Category> getCategoryList() {
        return foodMapper.getCategoryList(new Category());
    }

    @Override
    public void modifyCategory(Category category) {
        foodMapper.modifyCategory(category);
    }

    @Override
    public void addCategory(Category category) {
        foodMapper.addCategory(category);  //该种类下没有食品，可以删除。
    }

    @Override
    public void deleteCategoryById(Integer id) {

        /*先查询该种类下是否还有食品*/
        FoodDTO foodDTO = new FoodDTO();
        foodDTO.setCategory_id(id);
        List<Food> foodList = foodMapper.getFoodList(foodDTO);

        if (foodList != null && foodList.size() > 0)
            throw new CannotDeleteException("该种类下还有食品，不可删除！"); //该种类下有食品，不可以删除，抛出异常
        else
            foodMapper.deleteCategoryById(id);    //该种类下没有食品，可以直接删除
    }

    @Override
    public SaveVO getSaveListByUserid(Integer userid) {

        List<Save> saveList = saveMapper.getSaveListByUserId(userid);  //获得收藏列表

        SaveVO result = new SaveVO();  //创建返回结果

        //ids
        List<Integer> foods = new ArrayList<>();
        List<Integer> dishes = new ArrayList<>();

        /*分开food和dish*/
        for (Save save : saveList) {
            if(save.getType() == 1){  //food类型
                foods.add(save.getFoodId());
            }else{  //dish类型
                dishes.add(save.getFoodId());
            }
        }

        /*获得对应的food和dish*/
        if (foods.size() > 0)
            result.setFoods(foodMapper.getFoodListByIds(foods));
        if (dishes.size() >0)
            result.setDishes(dishMapper.getDishByIds(dishes));

        return result;
    }



    @Override
    public List<Suggestion> getTenRandomFoods() {

        /*随机获得10个整数作为要查的foodid*/
        int[] nums = new int[10];
        for(int i = 0; i < 10 ;i ++) {
            nums[i] = ThreadLocalRandom.current().nextInt(4141, 4421);
        }

        /*对title截取*/
        List<Suggestion> randomFoods = foodMapper.getRandomFoods(nums);
        for (Suggestion randomFood : randomFoods) {
            randomFood.setTitle(randomFood.getTitle().split("，")[0]);
        }
        return randomFoods;
    }

    @Override
    public FoodVO getRecommendedIntake(Integer id) {
        List<FoodVO> suggestion = foodMapper.getSuggestion(id);
        if(suggestion != null && suggestion.size() > 0)
            return suggestion.get(0);
        else
            return null;
    }



    @Override
    @Transactional
    public FoodVO setRecommendedIntake(IntakeVO intakeVO) {


        //修改用户信息
        userMapper.updateMessage(new User(intakeVO.getUserid(),null,null,null,null,null,intakeVO.getHeight(),intakeVO.getWeight(),null,null,intakeVO.getBirth(),intakeVO.getExercise(),intakeVO.getTarget()));

        int all = getAllIntake(intakeVO);  //基础热量消耗

        float intake = all * MessageConstant.EXERCISE[intakeVO.getExercise()]
                * MessageConstant.TARGET[intakeVO.getTarget()];  //运动量和健康目标

        if(intakeVO.getFat() != null) //如果体脂率不为null
            intake *= MessageConstant.FAT[intakeVO.getFat()];

        intake += intakeVO.getGym();  //加上健身消耗热量

        /*封装返回结果*/
        FoodVO result = new FoodVO();
        result.setCalories(intake);
        result.setCarbohydrate(intake * MessageConstant.TROPHIC_FACTOR[0] / 4);
        result.setProtein(intake * MessageConstant.TROPHIC_FACTOR[1] / 4);
        result.setFat(intake * MessageConstant.TROPHIC_FACTOR[2] / 9);
        result.setCellulose(25.0f);
        result.setUserid(intakeVO.getUserid());

        /*放到数据库中*/
        //判断数据库中有没有该用户的信息
        if(foodMapper.getSuggestion(intakeVO.getUserid()).size() == 0){
            //使用insert语句
            foodMapper.insertSuggestion(intakeVO.getUserid());
        }

        foodMapper.setSuggestion(result);  //执行更新语句

        return result;
    }
    private int getAllIntake(IntakeVO intakeVO){

        /*拿到计算所需的信息*/
        Float height = intakeVO.getHeight();
        Float weight = intakeVO.getWeight();
        int age = Period.between(intakeVO.getBirth(),LocalDate.now()).getYears();
        Integer exercise = intakeVO.getExercise();
        Integer target = intakeVO.getTarget();
        Integer sex = intakeVO.getSex();

        int all;
        if(sex == 1){//男性
            all = (int) ((int) 67 + 13.73 * weight + 5 * height - 6.9* age);
        }else{ //女性
            all = (int)((int) 661 + 9.7 * weight + 1.72 * height - 4.7 * age);
        }
        return all;
    }

    @Override
    public void deleteFood(Integer id) {
        foodMapper.deleteFood(id);
    }

    @Override
    public boolean canSave(Save save) {

        List<Save> canSave = foodMapper.getCanSave(save);
        //如果数据库中有此数据，说明不能再收藏了。
        return !(canSave != null && canSave.size() > 0);
    }

    @Override
    public void insertAFood(Food food) {
        foodMapper.insertAFood(food);
    }

    @Override
    public void doRmSave(RmSaveVO rmSaveVO) {
        foodMapper.doRmSave(rmSaveVO);
    }

    @Override
    public void updateFood(Food food) {
        foodMapper.updateFood(food);
    }

    @Override
    public void addSave(Save save) {
        saveMapper.addSave(save);
    }


    /**
     * @description: 获得添加的热量
     * @param: [com.healthyrecipes.pojo.vo.CalorieVO]
     * @return: void
     */
    @Override
    public void addCalorie(CalorieVO calorieVO) {
        // 0-4依次是：蛋白质 碳水 脂肪 纤维素 热量
        //key userId:type    type:list

        String key = "calories:"+calorieVO.getId();  //key

        /*封装信息*/
        Float[] value = new Float[5];
        value[0] = calorieVO.getProtein(); // 0 蛋白质
        value[1] = calorieVO.getCarbohydrate(); // 1 碳水
        value[2] = calorieVO.getFat(); //2 脂肪
        value[3] = calorieVO.getCellulose(); //  3 纤维素
        value[4] = calorieVO.getCalories(); // 4 卡路里



        //查看key是否存在
        if (redisUtil.llen(key) >  0) {
            //key 存在
            List<Object> lrange = redisUtil.lrange(key, 0, -1);  //获得原来的数据
            if(calorieVO.getOperator() == 0){
                for (int i = 0; i < 5; i++) {
                    value[i] = Float.parseFloat(lrange.get(i).toString()) - value[i];
                }
            }else{
                for (int i = 0; i < 5; i++) {
                    value[i] += Float.parseFloat(lrange.get(i).toString());
                }
            }
            redisUtil.del(key); //删除原来的key
        }

        //foodKey ---     food:userID:type   key(id)  value(g)    type:hash
        String foodKey = "food:"+calorieVO.getId()+":"+ calorieVO.getType();

        //判断数据库中有没有这个key
        if (redisUtil.hexists(foodKey,calorieVO.getFoodId())) {
            //有这个key,直接进行操作
            Float oldValue = Float.parseFloat(redisUtil.hget(foodKey,calorieVO.getFoodId().toString()).toString()); //原来的重量
            if(calorieVO.getOperator() == 0){
                //减法操作
                if(oldValue.equals(calorieVO.getG())){

                    redisUtil.hrmKek(foodKey,calorieVO.getFoodId());
                }else{
                    redisUtil.hset(foodKey,calorieVO.getFoodId().toString(), oldValue - calorieVO.getG());
                }
            }else{
                //加法操作
                redisUtil.hset(foodKey,calorieVO.getFoodId().toString(),calorieVO.getG() + oldValue);
            }

        }
        else{
            //没有这个key,直接添加操作
            redisUtil.hset(foodKey,calorieVO.getFoodId().toString(),calorieVO.getG());
        }

        //设置过期时间
        redisUtil.expire(foodKey,redisUtil.getSecondsToMidnight());

        redisUtil.rpushall(key, value); //放到Redis中去
        /*设置过期时间*/
        redisUtil.expire(key, redisUtil.getSecondsToMidnight());
    }

    @Override
    public CalorieResult getUserCalorie(Integer userid) {

        //获取热量信息
        CalorieResult result = new CalorieResult();
        result.setCalories(redisUtil.lrange("calories:"+userid ,0 , 5));

        //从redis中获取内容
        Map<Object, Object> breakfast = redisUtil.hgetall("food:" + userid + ":" + 0);
        Map<Object, Object> lunch = redisUtil.hgetall("food:" + userid + ":" + 1);
        Map<Object, Object> dinner = redisUtil.hgetall("food:" + userid + ":" + 2);

        //封装信息
        result.setBreakfast(fullFoodMessage(breakfast));
        result.setLunch(fullFoodMessage(lunch));
        result.setDinner(fullFoodMessage(dinner));

        return result;
    }

    /**
     * @description: 封装食物信息 + g
     * @param: [java.util.Map<java.lang.Object,java.lang.Object>]
     * @return: java.util.List<com.healthyrecipes.pojo.dto.MessageDTO>
     */
    private List<MessageDTO> fullFoodMessage(Map<Object,Object> foods){

        //ids
        List<Object> ids = new ArrayList<>(foods.keySet());
        List<Integer> ids_int = new ArrayList<>();

        //变成Integer类型
        for (Object id : ids) {
            ids_int.add(Integer.valueOf((String) id));
        }

        ids = null;  //help GC

        //重量信息
        List<Object> values = new ArrayList<>(foods.values());

        List<MessageDTO> message = null;
        if(ids_int.size() > 0)
            message = foodMapper.getMessage(ids_int);
        else
            return null;

        //将重量表中的数据放到message中
        for(int i = 0 ;i < message.size(); i++){
            message.get(i).setG(Float.parseFloat(values.get(i).toString()));
        }

        return message;
    }

}
