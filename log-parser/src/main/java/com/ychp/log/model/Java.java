package com.ychp.log.model;

import lombok.Data;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/12/07
 */
@Data
public class Java {

    private String timeStr;

    private String level;

    private String classFullName;

    private String classLine;

    private String msg;

}
