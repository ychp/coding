package com.ychp.utils.dto;

import lombok.Data;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/3/10
 */
@Data
public class COSDto {

    private int code;

    private String message;

    private String request_id;

    private DataDto data;

}
