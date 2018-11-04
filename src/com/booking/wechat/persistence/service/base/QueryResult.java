package com.booking.wechat.persistence.service.base;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询结果对象
 * 
 * @author Luoxx
 * @param <T>
 */
public class QueryResult<T> {

    private List<T> rows;

    private Long    total;

    /**
     * 当查询记录数为0时,返回一个空的ArrayList实例而不是NULL，
     * 
     * @return
     */
    public List<T> getRows() {
        if (rows != null) {
            return rows;
        }
        else {
            return new ArrayList<T>();
        }
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
