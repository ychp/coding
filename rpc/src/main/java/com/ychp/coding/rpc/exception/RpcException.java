package com.ychp.coding.rpc.exception;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/9/21
 */
public class RpcException extends RuntimeException {

    public RpcException(String message){
        super(message);
    }
}
