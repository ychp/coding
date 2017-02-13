package com.ychp.log.parser;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ychp.log.model.Nginx;
import com.ychp.log.model.UserAgent;
import com.ychp.log.utils.UaUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/9/30
 */
@Slf4j
public class NginxLogParser extends Parser<Nginx> {

    private static final String DOMAIN = "www.yingchengpeng.com";

    @Override
    public Nginx parserLine(String content) {
        Nginx nginx = new Nginx();
        String [] strArr = content.split("\"");

        String [] ipAndTime = strArr[0].split("\\[");
        nginx.setIp(ipAndTime[0].split(" ")[0]);
        nginx.setTimeStr(ipAndTime[1].split("\\]")[0]);

        String[] uriInfo = strArr[1].split(" ");
        nginx.setMethod(uriInfo[0]);
        nginx.setUri(uriInfo[1]);
        nginx.setProtocol(uriInfo[2]);
        nginx.setHttpCode(Integer.valueOf(strArr[2].split(" ")[1]));
        nginx.setContentLength(Integer.valueOf(strArr[2].split(" ")[2]));

        nginx.setUrl(strArr[3]);
        if(nginx.getUrl().contains("/")) {
            String domain = nginx.getUrl().split("/")[2];
            nginx.setDomain(domain);
        }
        nginx.setUa(strArr[5]);

        UserAgent userAgent = UaUtil.parseUa(nginx.getUa());
        nginx.setUserAgent(userAgent);

        return nginx;
    }

    @Override
    public void summary() {
        log.info("summary start");
        summaryDatas = Maps.newHashMap();
        uaWithIp = Maps.newHashMap();
        Map<String, Long> ipCount = Maps.newTreeMap();
        Map<String, Long> urlCount = Maps.newTreeMap();
        Map<String, Long> ipAndUrlCount = Maps.newTreeMap();
        Map<String, Long> ipAndDomainCount = Maps.newTreeMap();
        for(Nginx nginx : datas){
            ipCount.put(nginx.getIp(), ipCount.get(nginx.getIp()) == null ? 1 : ipCount.get(nginx.getIp()) + 1);
            urlCount.put(nginx.getUrl(), urlCount.get(nginx.getUrl()) == null ? 1 : urlCount.get(nginx.getUrl()) + 1);
            ipAndUrlCount.put(nginx.getIp()+","+nginx.getUrl(), ipAndUrlCount.get(nginx.getIp()+","+nginx.getUrl()) == null ? 1 : ipAndUrlCount.get(nginx.getIp()+","+nginx.getUrl()) + 1);
            ipAndDomainCount.put(nginx.getIp()+","+nginx.getDomain(), ipAndDomainCount.get(nginx.getIp()+","+nginx.getDomain()) == null ? 1 : ipAndDomainCount.get(nginx.getIp()+","+nginx.getDomain()) + 1);
            uaWithIp.put(nginx.getIp(), nginx.getUa());
        }
        summaryDatas.put("ip", ipCount);
        summaryDatas.put("url", urlCount);
        summaryDatas.put("ipAndUrl", ipAndUrlCount);
        summaryDatas.put("ipAndDomain", ipAndDomainCount);
        log.info("summary end");
    }

    @Override
    public void printAll() {
        log.info("print start");
        printOne("/Users/yingchengpeng/nginx/ipCount.txt", "ip");
        printOne("/Users/yingchengpeng/nginx/urlCount.txt", "url");
        printOne("/Users/yingchengpeng/nginx/ipAndUrlCount.txt", "ipAndUrl");
        printOne("/Users/yingchengpeng/nginx/ipAndDomainCount.txt", "ipAndDomain");
        printIpNotAllow("/Users/yingchengpeng/nginx/iptables.txt");
        printUa("/Users/yingchengpeng/nginx/blackUa.txt");
        log.info("print end");
    }

    @Override
    public void printIpNotAllow(String path) {
        blackIp = Lists.newArrayList();
        initOutputStream(path);
        String line;
        Map<String,Long> ipAndUrl = summaryDatas.get("ipAndDomain");
        List<Map.Entry<String,Long>> list = new ArrayList<Map.Entry<String,Long>>(ipAndUrl.entrySet());
        //然后通过比较器来实现排序
        //升序排序
        list.sort((o1, o2) -> -o1.getValue().compareTo(o2.getValue()));

        BufferedWriter writer = getWriter();
        try {
            for(Map.Entry<String,Long> entry:list){
                String ipAndDomain = entry.getKey();
                String ip = ipAndDomain.split(",")[0];
                String domain = ipAndDomain.split(",")[1];
                if(DOMAIN.equals(domain))
                    continue;
                blackIp.add(ip);
                line = "iptables -I INPUT -s " + ip + " -j DROP";
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void printUa(String path) {
        initOutputStream(path);
        String line;
        BufferedWriter writer = getWriter();
        try {

            for(Map.Entry<String,String> entry:uaWithIp.entrySet()){
                boolean isTool = entry.getValue().toLowerCase().contains("curl")
                        || entry.getValue().toLowerCase().contains("scrapy")
                        || entry.getValue().toLowerCase().contains("httpclient")
                        || entry.getValue().toLowerCase().contains("wget");
                if(blackIp.contains(entry.getKey()) && !"_".equals(entry.getValue()) && !isTool) {
                    line = entry.getValue() + "|";
                    writer.write(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args){
        NginxLogParser parser = new NginxLogParser();
        parser.setFile("/Users/yingchengpeng/404.txt");
        parser.parserAll();
        parser.summary();
        parser.printAll();
    }
}
