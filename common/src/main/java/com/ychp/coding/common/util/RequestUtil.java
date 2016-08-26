package com.ychp.coding.common.util;

import com.ychp.coding.common.model.BaiduIpAddress;
import com.ychp.coding.common.model.IpAddress;
import com.ychp.coding.common.model.TaobaoIpAddress;

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

    private static final String BAIDU_IP_API_URL = "http://apis.baidu.com/apistore/iplookupservice/iplookup";

    private static final String TAOBAO_IP_API_URL = "http://ip.taobao.com/service/getIpInfo.php";

    private static final String BAIDU_API_KEY = "24bd43016ccf30255ceac63669d7684c";

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


    /**
     * @param httpArg
     *            :参数
     * @return 返回结果
     */
    public static IpAddress baiduIpAddress(String httpArg) {
        BufferedReader reader = null;
        String str = null;
        StringBuffer sbf = new StringBuffer();
        String requestUrl = BAIDU_IP_API_URL + "?" + httpArg;
        BaiduIpAddress ipAddress = null;
        IpAddress result = new IpAddress();
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey",  BAIDU_API_KEY);
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            str = sbf.toString();
            ipAddress = JsonMapper.JSON_NON_DEFAULT_MAPPER.fromJson(str, BaiduIpAddress.class);
            if(ipAddress.getErrNum() == 0) {
                result.setSuccess(true);
                result.setProvince(ipAddress.getRetData().getProvince());
                result.setCity(ipAddress.getRetData().getCity());
                result.setCountry(ipAddress.getRetData().getCountry());
                result.setIsp(ipAddress.getRetData().getCarrier());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param httpArg
     *            :参数
     * @return 返回结果
     */
    public static IpAddress taobaoIpAddress(String httpArg) {
        BufferedReader reader = null;
        String str = null;
        StringBuffer sbf = new StringBuffer();
        String requestUrl = TAOBAO_IP_API_URL + "?" + httpArg;
        TaobaoIpAddress ipAddress = null;
        IpAddress result = new IpAddress();
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            str = sbf.toString();
            ipAddress = JsonMapper.JSON_NON_DEFAULT_MAPPER.fromJson(str, TaobaoIpAddress.class);
            if(ipAddress.getCode()==0) {
                result.setSuccess(true);
                result.setProvince(ipAddress.getData().getRegion());
                result.setCity(ipAddress.getData().getCity());
                result.setCountry(ipAddress.getData().getCountry());
                result.setIsp(ipAddress.getData().getIsp());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
