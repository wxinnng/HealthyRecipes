package com.healthyrecipes.exception;



/**
 * @Author:86198
 * @DATE:2024/3/10 10:54
 * @DESCRIPTION:
 * @VERSION:1.0
 */
public class NoSuchCommentException extends Exception{
    public NoSuchCommentException(){}
    public NoSuchCommentException(String msg){
        super(msg);
    }
}
