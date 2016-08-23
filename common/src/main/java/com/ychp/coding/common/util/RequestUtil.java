package com.ychp.coding.common.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/8/23
 */
public class RequestUtil {

    public static String getIp(HttpServletRequest request){
        return request.getRemoteAddr();
    }

    public static String getHost(HttpServletRequest request){
        return request.getRemoteHost();
    }
}
