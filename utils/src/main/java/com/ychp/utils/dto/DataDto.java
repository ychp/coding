package com.ychp.utils.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/3/10
 */
@Data
public class DataDto implements Serializable {
    private static final long serialVersionUID = 6756135568299088469L;

    private String context;

    private List<InfoDto> infos;

    private Boolean listover;
}
