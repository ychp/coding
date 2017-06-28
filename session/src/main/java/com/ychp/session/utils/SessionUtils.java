package com.ychp.session.utils;

import java.util.UUID;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/12/31
 */
public class SessionUtils {

    public static final String SESSION_KEY = "msid";

    public static final int EXPIRE_SECONDS = 3600;

    public static String getSessionId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getSessionKey(String sessionId){
        return "session:" + sessionId;
    }

    public static String getSessionKey(String space, Object token){
        return "session:" + space + ":" + token.toString();
    }

    public static String getSessionMatchKey(){
        return "session:*";
    }

    public static String getSessionMatchKey(String space){
        return "session:" + space + ":*";
    }

}
