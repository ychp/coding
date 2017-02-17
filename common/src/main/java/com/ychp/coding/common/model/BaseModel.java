package com.ychp.coding.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/12/8
 */
@Data
public class BaseModel implements Serializable {

    private static final long serialVersionUID = -4557491559890306857L;

    private Long id;

    private Date createdAt;

    private Date updatedAt;
}
