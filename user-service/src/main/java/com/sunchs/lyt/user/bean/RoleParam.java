package com.sunchs.lyt.user.bean;

import java.util.List;

public class RoleParam {

    public Integer roleId;
    public String title;
    public List<NodeActionParam> node;

    public Integer getRoleId() {
        return roleId;
    }

    public String getTitle() {
        return title;
    }

    public List<NodeActionParam> getNode() {
        return node;
    }
}
