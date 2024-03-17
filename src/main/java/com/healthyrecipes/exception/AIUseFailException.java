package com.healthyrecipes.exception;

/**
 * @Author:86198
 * @DATE:2024/3/8 21:42
 * @DESCRIPTION: AI使用错误
 * @VERSION:1.0
 */
public class AIUseFailException extends Exception{
    public AIUseFailException(){}
    public AIUseFailException(String msg ){
        super(msg);
    }
}
