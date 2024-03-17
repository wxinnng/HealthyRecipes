package com.healthyrecipes.exception;

/**
 * @Author:86198
 * @DATE:2023/12/30 13:33
 * @DESCRIPTION: 业务异常基类，以后有了新的异常类可以以继承这个类
 * @VERSION:1.0
 */
public class BaseException extends RuntimeException{
    public BaseException(){}
    public BaseException(String msg){
        super(msg);
    }
}
