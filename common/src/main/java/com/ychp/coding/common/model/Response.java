package com.ychp.coding.common.model;

import lombok.Getter;

import java.io.Serializable;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/7/16
 */
public class Response<T> implements Serializable{

    private static final long serialVersionUID = -6721355847035902529L;

    private Boolean success = false;

    public static <T> Response<T> ok(T t){
        return new Response<>(t);
    }

    public static <T> Response<T> fail(String error){
        return new Response<>(error);
    }

    public Response(){}

    private Response(T result){
        success = true;
        this.result = result;
    }

    private Response(String error){
        success = false;
        this.error = error;
    }

    @Getter
    private String error;

    @Getter
    private T result;

    public void setError(String error){
        this.error = error;
        success = false;
    }

    public void setResult(T t){
        this.result = t;
        success = true;
    }

    public Boolean isSuccess(){
        return this.success;
    }
}
