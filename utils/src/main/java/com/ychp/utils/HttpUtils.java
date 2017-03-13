package com.ychp.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ychp.utils.dto.COSDto;
import com.ychp.utils.dto.InfoDto;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/3/10
 */
public class HttpUtils {

    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("Authorization", "Xox/JA3rXqfaNFiZ/v1xIiedBP5hPTEyNTI3NTEyODYmaz1BS0lEaUtrZlhGWkJRVE1TVnpSYWRUN2YzSWRWR29BS3Q0d2gmZT0xNDg5MTY1MjE5JnQ9MTQ4OTE1OTIxOSZyPTQ2MDcyNTQyMSZmPS8xMjUyNzUxMjg2L28yby8mYj1vMm8=");
            connection.setRequestProperty("Host", "web.file.myqcloud.com");
            connection.setRequestProperty("Content-Type", "application/json");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        String path = "/Users/yingchengpeng/myWorks/coding/utils/src/main/java/com/ychp/utils";
        BufferedWriter bufferedWriter = null;
        String fileName = path + "result.txt";
        File file = new File(fileName);
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        String result;
        ObjectMapper objectMapper = new ObjectMapper();
        COSDto cosDto;
        List<InfoDto> infos;
        int size = 1000;
        String context = "";

        while (size == 1000){
            result = sendGet("http://gz.file.myqcloud.com/files/v2/1252751286/o2o/", "op=list&order=0&num=1000&context=" + context);
            cosDto = objectMapper.readValue(result, COSDto.class);
            infos = cosDto.getData().getInfos();
            size = infos.size();
            System.out.println("size: " + size);
            context = cosDto.getData().getContext();
            System.out.println("context: " + context);
            System.out.println("===========");
            for(InfoDto info : infos){
                if(info.getName().toLowerCase().endsWith("/")) {
                    bufferedWriter.write(info.getName());
                    bufferedWriter.newLine();
                    System.out.println(info.getName());
                }
            }
        }



    }
}
