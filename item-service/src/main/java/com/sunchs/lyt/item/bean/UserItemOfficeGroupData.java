package com.sunchs.lyt.item.bean;

import java.util.List;

public class UserItemOfficeGroupData {

    private String groupName;

    private List<UserItemOfficeData> officeList;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<UserItemOfficeData> getOfficeList() {
        return officeList;
    }

    public void setOfficeList(List<UserItemOfficeData> officeList) {
        this.officeList = officeList;
    }
}
