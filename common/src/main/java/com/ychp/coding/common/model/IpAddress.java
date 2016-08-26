package com.ychp.coding.common.model;

import lombok.Data;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/8/26
 */
@Data
public class IpAddress {

    private String country;

    private String province;

    private String city;

    private String isp;

}
