package com.ychp.code.builder.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/12
 */
@Data
public class MybatisColumnDto implements Serializable {

    private static final long serialVersionUID = 2108590294820164094L;

    private String sqlColumn;

    private String javaXmlColumn;

    private String javaColumn;

    private String javaType;
}
