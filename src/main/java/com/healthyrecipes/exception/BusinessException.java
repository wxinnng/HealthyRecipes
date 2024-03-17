package com.healthyrecipes.exception;

/**
 * @Author:86198
 * @DATE:2024/3/17 18:40
 * @DESCRIPTION: 业务异常
 * @VERSION:1.0
 */
public class BusinessException extends BaseException{
    public BusinessException(){}
    public BusinessException(String msg){
        super(msg);
    }
}
