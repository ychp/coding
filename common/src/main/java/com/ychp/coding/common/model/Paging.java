package com.ychp.coding.common.model;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/12/17
 */
@Data
@NoArgsConstructor
public class Paging<T> {

    public Paging(Integer pageNo, Integer pageSize){
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    private List<T> datas = Lists.newArrayList();

    private Integer pageNo = 1;

    private Integer pageSize = 20;

    private Long total = 0L;
}
