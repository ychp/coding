package com.ychp.coding.common.util;

import com.google.common.base.Objects;
import com.ychp.coding.common.enums.IPAPIType;
import com.ychp.coding.common.model.BaiduIpAddress;
import com.ychp.coding.common.model.IpAddress;
import com.ychp.coding.common.model.SinaIpAddress;
import com.ychp.coding.common.model.TaobaoIpAddress;
import com.ychp.log.model.UserAgent;
import com.ychp.log.utils.UaUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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
@Slf4j
public class RequestUtil {

    private static final String BAIDU_IP_API_URL = "http://apis.baidu.com/apistore/iplookupservice/iplookup?ip=";

    private static final String TAOBAO_IP_API_URL = "http://ip.taobao.com/service/getIpInfo.php?ip=";

    private static final String SINA_IP_API_URL = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=";

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
     * 获取ip归属地
     */
    public static IpAddress getIpAddress(String ip, Integer type, String apiKey){
        IpAddress result;
        if(type == null){
            result = taobaoIpAddress(ip);
        }else if(Objects.equal(IPAPIType.BAIDU.value(), type)){
            result = baiduIpAddress(ip, apiKey);
        }else if(Objects.equal(IPAPIType.TAOBAO.value(), type)){
            result = taobaoIpAddress(ip);
        }else if(Objects.equal(IPAPIType.SINA.value(), type)){
            result = sinaIpAddress(ip);
        }else{
            result = taobaoIpAddress(ip);
        }
        if(StringUtils.isEmpty(result.getProvince())){
            result.setProvince(result.getCountry());
        }
        if(StringUtils.isEmpty(result.getCity())){
            result.setCity(result.getCountry());
        }
        return result;
    }


    /**
     * 获取ip归属地(百度)
     */
    private static IpAddress baiduIpAddress(String ip, String apiKey) {
        IpAddress result = new IpAddress();
        String str = get(BAIDU_IP_API_URL + ip, true, apiKey);
        BaiduIpAddress ipAddress = JsonMapper.JSON_NON_DEFAULT_MAPPER.fromJson(str, BaiduIpAddress.class);
        if(ipAddress != null  && ipAddress.getErrNum() == 0) {
            result.setSuccess(true);
            if(ipAddress.getRetData() != null) {
                result.setProvince(ipAddress.getRetData().getProvince());
                result.setCity(ipAddress.getRetData().getCity());
                result.setCountry(ipAddress.getRetData().getCountry());
                result.setIsp(ipAddress.getRetData().getCarrier());
            }else{
                log.info("ipAddress : {}", ipAddress);
            }
        }

        return result;
    }

    /**
     * 获取ip归属地(淘宝)
     */
    private static IpAddress taobaoIpAddress(String ip) {
        IpAddress result = new IpAddress();
        String str = get(TAOBAO_IP_API_URL + ip, false, null);
        TaobaoIpAddress ipAddress = JsonMapper.JSON_NON_DEFAULT_MAPPER.fromJson(str, TaobaoIpAddress.class);
        if(ipAddress != null  && ipAddress.getCode()==0) {
            result.setSuccess(true);
            if(ipAddress.getData() != null){
                result.setProvince(ipAddress.getData().getRegion());
                result.setCity(ipAddress.getData().getCity());
                result.setCountry(ipAddress.getData().getCountry());
                result.setIsp(ipAddress.getData().getIsp());
            }else{
                log.info("ipAddress : {}", ipAddress);
            }
        }
        return result;
    }

    /**
     * 获取ip归属地(新浪)
     */
    private static IpAddress sinaIpAddress(String ip) {
        IpAddress result = new IpAddress();
        String str = get(SINA_IP_API_URL + ip, false, null);
        SinaIpAddress ipAddress = JsonMapper.JSON_NON_DEFAULT_MAPPER.fromJson(str, SinaIpAddress.class);
        if(ipAddress != null  && ipAddress.getRet()==1) {
            result.setSuccess(true);
            result.setProvince(ipAddress.getProvince());
            result.setCity(ipAddress.getCity());
            result.setCountry(ipAddress.getCountry());
            result.setIsp(ipAddress.getIsp());
        }
        return result;
    }

    private static String get(String requestUrl, Boolean isCheck, String apiKey){
        BufferedReader reader;
        String str = null;
        StringBuilder sbf = new StringBuilder();
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            if(isCheck) {
                connection.setRequestProperty("apikey", apiKey);
            }
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            str = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static UserAgent getUaInfo(HttpServletRequest request){
        String ua = request.getHeader("User-Agent");
        UserAgent userAgent = UaUtil.parseUa(ua);
        userAgent = userAgent == null ? new UserAgent() : userAgent;
        return userAgent;
    }

    public static String getUrl(HttpServletRequest request){
        return request.getRequestURL().toString();
    }

}
