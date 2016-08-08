package com.ychp.coding.common.exception;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/7/30
 */
public class ServiceException extends RuntimeException {

    public ServiceException(String error){
        super(error);
    }
}
