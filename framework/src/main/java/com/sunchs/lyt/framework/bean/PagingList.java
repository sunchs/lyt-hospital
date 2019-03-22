package com.sunchs.lyt.framework.bean;

import java.util.ArrayList;
import java.util.List;

public class PagingList<T> {

    public Integer total;// 总条数
    public Integer pages;// 总页数
    public Integer pageNow;// 当前页数
    public Integer pageSize;// 每页条数
    public List<T> list = new ArrayList<>();// 列表数据

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public void setPageNow(Integer pageNow) {
        this.pageNow = pageNow;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}