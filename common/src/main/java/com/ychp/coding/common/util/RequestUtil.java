package com.ychp.coding.common.util;

import com.ychp.coding.common.model.IpAddress;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/8/23
 */
public class RequestUtil {

    public static String getIp(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getHost(HttpServletRequest request){
        return request.getRemoteHost();
    }

    private static final String httpUrl = "http://apis.baidu.com/apistore/iplookupservice/iplookup";

    private static final String apiKey = "http://apis.baidu.com/apistore/iplookupservice/iplookup";

    /**
     * @param httpArg
     *            :参数
     * @return 返回结果
     */
    public static IpAddress getIpAddress(String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        String requestUrl = httpUrl + "?" + httpArg;
        IpAddress ipAddress = null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey",  apiKey);
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
            ipAddress = JsonMapper.JSON_NON_DEFAULT_MAPPER.fromJson(result, IpAddress.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipAddress;
    }

}