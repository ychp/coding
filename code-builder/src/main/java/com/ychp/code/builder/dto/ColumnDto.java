package com.ychp.code.builder.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/10
 */
@Data
@NoArgsConstructor
public class ColumnDto implements Serializable {

    private static final long serialVersionUID = -1787701137518696872L;

    public ColumnDto(String name, String dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    private String name;

    private String dataType;

}