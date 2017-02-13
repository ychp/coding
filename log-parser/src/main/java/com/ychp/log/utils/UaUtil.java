package com.ychp.log.utils;

import com.ychp.log.model.UserAgent;
import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;

import java.io.IOException;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/10/1
 */
public class UaUtil {

    public static UserAgent parseUa(String ua){
        UserAgentInfo userAgentInfo = null;
        try {
            userAgentInfo = new UASparser(OnlineUpdater.getVendoredInputStream()).parse(ua);
        } catch (IOException e) {
            e.printStackTrace();
        }
        UserAgent userAgent = new UserAgent();
        userAgent.setSystem(userAgentInfo != null ? userAgentInfo.getOsFamily() : null);
        userAgent.setSystemName(userAgentInfo != null ? userAgentInfo.getOsName() : null);
        userAgent.setBrowserName(userAgentInfo != null ? userAgentInfo.getUaFamily() : null);
        userAgent.setBrowserVersion(userAgentInfo != null ? userAgentInfo.getBrowserVersionInfo() : null);
        userAgent.setDevice(userAgentInfo != null ? userAgentInfo.getDeviceType() : null);
        userAgent.setBrowser(userAgentInfo != null ? userAgentInfo.getUaName() : null);
        userAgent.setType(userAgentInfo != null ? userAgentInfo.getType() : null);
        return userAgent;
    }
}
