package com.sunchs.lyt.user.bean;

import java.util.List;

public class RoleNodeData {

    public Integer roleId;
    public String title;
    public List<NodeData> nodeList;

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

    public List<NodeData> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<NodeData> nodeList) {
        this.nodeList = nodeList;
    }
}