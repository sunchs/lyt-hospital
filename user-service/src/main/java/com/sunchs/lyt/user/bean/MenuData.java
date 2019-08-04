package com.sunchs.lyt.user.bean;

import com.sunchs.lyt.db.business.entity.Node;

import java.util.List;

public class MenuData extends Node {

    private List<MenuData> children;

    public List<MenuData> getChildren() {
        return children;
    }

    public void setChildren(List<MenuData> children) {
        this.children = children;
    }
}
