package com.ychp.coding.common.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Desc: 分页插件
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/7/16
 */
public class Paging<T> implements Serializable{

    private static final long serialVersionUID = -9191871345855243023L;

    @Setter@Getter
    private Integer total;

    @Setter@Getter
    private List<T> data;

    @Getter
    private Boolean isEmpty = true;

    @Setter@Getter
    private Integer pageNo = 1;

    @Setter@Getter
    private Integer pageSize = 20;

    public Paging(){}

    public Paging(List data){
        this.data = data;
        if (data != null && data.size()>0){
            this.isEmpty = false;
        }
    }

    public Paging(List data, int total){
        this.data = data;
        if (data != null && data.size()>0){
            this.isEmpty = false;
        }
        this.total = total;
    }

    public Integer getOffset(){
        Integer offset = Integer.valueOf((pageNo.intValue() - 1) * pageSize.intValue());
        return Integer.valueOf(offset.intValue() > 0 ? offset.intValue():0);
    }

    public Integer getLimit(){
        return Integer.valueOf(pageSize.intValue() > 0 ? pageSize.intValue():20);
    }
}
