package com.ychp.coding.common.mysql;

import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Mail:ychp@terminus.io
 * Date: 16/4/17
 * Author: yingchengpeng
 */
public interface MybatisDao<T> {

    public Integer insert(T t);

    public Integer inserts(List<T> t);

    public Integer update(T t);

    public T selectById(Long id);

    public List<T> paging(Map<String, Object> criteria);

    public Integer count(Map<String, Object> criteria);

    public Integer delete(Long id);
}
