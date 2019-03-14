package com.sunchs.lyt.user.bean;

import java.util.List;
import java.util.Map;

public class RoleParam {

    public Integer roleId;
    public String title;
    public List<Map<String,Integer>> access;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Map<String, Integer>> getAccess() {
        return access;
    }

    public void setAccess(List<Map<String, Integer>> access) {
        this.access = access;
    }
}
