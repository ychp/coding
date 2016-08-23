package com.ychp.coding.common.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/8/23
 */
public class RequestUtil {

    public String getIp(HttpServletRequest request){
        return request.getRemoteAddr();
    }

    public String getHost(HttpServletRequest request){
        return request.getRemoteHost();
    }
}
