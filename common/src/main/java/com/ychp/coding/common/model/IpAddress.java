package com.ychp.coding.common.model;

import lombok.Data;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/8/25
 */
@Data
public class IpAddress {

    private Integer errNum;

    private String errMsg;

    private RetData retData;

    @Data
    public class RetData{

        private String ip;

        private String country;

        private String province;

        private String city;

        private String district;

        private String carrier;
    }
}
