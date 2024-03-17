package com.healthyrecipes.common.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @param <T>
 */
@Data
public class ResultJson<T> implements Serializable {

    private Integer code; //编码：1成功，0和其它数字为失败
    private String msg; //错误信息
    private T data; //数据

    public static <T> ResultJson<T> success() {
        ResultJson<T> resultJson = new ResultJson<T>();
        resultJson.code = 1;
        return resultJson;
    }

    public static <T> ResultJson<T> success(T object) {
        ResultJson<T> resultJson = new ResultJson<T>();
        resultJson.data = object;
        resultJson.code = 1;
        return resultJson;
    }

    public static <T> ResultJson<T> error(String msg) {
        ResultJson resultJson = new ResultJson();
        resultJson.msg = msg;
        resultJson.code = 0;
        return resultJson;
    }

}
