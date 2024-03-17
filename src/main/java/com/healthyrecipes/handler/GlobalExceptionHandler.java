package com.healthyrecipes.handler;

import com.healthyrecipes.common.result.ResultJson;
import com.healthyrecipes.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author:86198
 * @DATE:2023/12/30 13:40
 * @DESCRIPTION:
 * @VERSION:1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * @description: 统一处理异常
     * @param: [com.healthyrecipes.exception.BaseException]
     * @return: com.healthyrecipes.common.result.Result
     */
    @ExceptionHandler
    public ResultJson<String> exceptionHandler(BaseException exception){
        log.info("异常信息：{}",exception.getMessage());
        return ResultJson.error(exception.getMessage());
    }

}
