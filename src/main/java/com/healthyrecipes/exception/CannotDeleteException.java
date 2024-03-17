package com.healthyrecipes.exception;

/**
 * @Author:86198
 * @DATE:2024/2/21 12:26
 * @DESCRIPTION: 不可删除异常
 * @VERSION:1.0
 */
public class CannotDeleteException extends BaseException{
    public CannotDeleteException(){}
    public CannotDeleteException(String msg){
        super(msg);
    }
}
