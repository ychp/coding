package com.ychp.coding.common.model;

import lombok.Data;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/8/27
 */
@Data
public class SinaIpAddress {

    private Integer ret;

    private Integer start;

    private Integer end;

    private String country;

    private String province;

    private String city;

    private String district;

    private String isp;

    private String type;

    private String desc;

}
