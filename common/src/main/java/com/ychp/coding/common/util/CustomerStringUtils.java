package com.ychp.coding.common.util;

import java.util.Random;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/12/13
 */
public class CustomerStringUtils {

    public final static String SPLIT_CHARACTER = ",";

    public static String formatString(String template, String ... params){
        String formatStr = template;
        for(int i = 0; i < params.length; i++){
            formatStr = formatStr.replace("{" + i + "}", params[i]);
        }
        return formatStr;
    }

    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
