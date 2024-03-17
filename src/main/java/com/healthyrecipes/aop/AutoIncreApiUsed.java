package com.healthyrecipes.aop;

import com.healthyrecipes.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @Author:86198
 * @DATE:2024/3/2 16:20
 * @DESCRIPTION: 自定义切面实现api调用次数的增加
 * @VERSION:1.0
 */
@Aspect
@Component
@Slf4j
public class AutoIncreApiUsed {

    @Resource
    private RedisUtil redisUtil;

    @Pointcut("execution(public * com.healthyrecipes.controller..*.*(..))")
    public void pointCut() {}

    @Before("pointCut()")
    public void before(JoinPoint joinPoint){
        log.info("Api 调用");
        redisUtil.incrby("msg:apiUsed",1); //接口访问数量 ++
        redisUtil.expire("msg:apiUsed",getSecondsToMidnight()); //设置过期时间
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

}
