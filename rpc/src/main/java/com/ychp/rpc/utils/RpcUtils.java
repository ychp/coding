package com.ychp.rpc.utils;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/14
 */
public class RpcUtils {

    public static <T> T firstNonNull(T first, T second) {
        return first != null?first:checkNotNull(second);
    }

    public static <T> T checkNotNull(T reference) {
        if(reference == null) {
            throw new NullPointerException();
        } else {
            return reference;
        }
    }
}
