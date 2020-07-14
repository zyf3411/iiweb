package com.sunnyz.iiwebapi.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageDto<T> implements Serializable {
    /**
     * 当前页
     */
    private int page;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 当前页纪录
     */
    private List<T> rows;

    public PageDto(int page, long total, List<T> rows) {
        this.page = page;
        this.total = total;
        this.rows = rows;
    }

    public PageDto() {
        this(0, 0, new ArrayList<>());
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
