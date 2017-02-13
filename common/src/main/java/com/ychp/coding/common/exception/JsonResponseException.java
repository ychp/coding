package com.ychp.coding.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/7/31
 */
public class JsonResponseException extends RuntimeException {

    @Setter@Getter
    private int status = 500;
    @Setter@Getter
    private String message = "unknow error";

    public JsonResponseException(){}

    public JsonResponseException(String message) {
        this.message = message;
    }

    public JsonResponseException(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
