package com.ychp.coding.common.model;

import lombok.Data;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/8/26
 */
@Data
public class TaobaoIpAddress {

    private Integer code;

    private TabaoData data;

    @Data
    public class TabaoData{

        private String country;

        private String country_id;

        private String area;

        private String area_id;

        private String region;

        private String region_id;

        private String city;

        private String city_id;

        private String county;

        private String county_id;

        private String isp;

        private String isp_id;

        private String ip;
    }
}
