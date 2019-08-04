package com.sunchs.lyt.user.bean;

import com.sunchs.lyt.framework.bean.PagingList;

import java.util.List;

public class RoleParam extends PagingList {

    private int id;
    private String title;
    private List<NodeActionParam> node;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<NodeActionParam> getNode() {
        return node;
    }
}
