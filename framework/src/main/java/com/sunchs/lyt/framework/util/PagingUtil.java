package com.sunchs.lyt.framework.util;

import com.sunchs.lyt.framework.bean.PagingList;

import java.util.List;

public class PagingUtil {

    public static int getSkip(int pageNow, int pageSize) {
        return (pageNow - 1) * pageSize;
    }

    public static <T> PagingList<T> getData(List<T> list, int total, int pageNow, int pageSize) {
        if (pageSize == 0) {
            throw new RuntimeException("每页条数不能为零");
        }
//        System.out.println(total);
//        System.out.println(pageSize);
//        System.out.println(total / pageSize);
//        System.out.println(Math.ceil(total / pageSize));
        int pages = (int) Math.ceil(total / pageSize);
        PagingList<T> page = new PagingList<>();
        page.setTotal(total);
        page.setPages(pages);
        page.setPageNow(pageNow);
        page.setPageSize(pageSize);
        page.setList(list);
        return page;
    }

}