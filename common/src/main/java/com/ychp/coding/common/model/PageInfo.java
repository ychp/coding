package com.ychp.coding.common.model;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/12/9
 */
@Data
@NoArgsConstructor
public class PageInfo {

    private Integer pageNo = 1;

    private Integer pageSize = 20;

    public PageInfo(Integer pageNo, Integer pageSize){
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public Integer getOffset(){
        Integer no = pageNo == null ? 1: pageNo;
        Integer size = pageSize == null ? 20: pageSize;
        return (no - 1) * size;
    }

    public Integer getLimit(){
        return pageSize == null ? 20: pageSize;
    }

    public Map<String,Object> putIntoMap(Map<String,Object> map){
        if(map == null){
            map = Maps.newHashMap();
        }
        map.put("offset", getOffset());
        map.put("limit", getLimit());
        return map;
    }
}
