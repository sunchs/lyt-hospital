package com.sunchs.lyt.item.bean;

import java.util.List;

public class UserItemOfficeTypeData {

    private int officeTypeId;
    private String officeTypeName;
    private List<UserItemOfficeGroupData> groupList;

    public int getOfficeTypeId() {
        return officeTypeId;
    }

    public void setOfficeTypeId(int officeTypeId) {
        this.officeTypeId = officeTypeId;
    }

    public String getOfficeTypeName() {
        return officeTypeName;
    }

    public void setOfficeTypeName(String officeTypeName) {
        this.officeTypeName = officeTypeName;
    }

    public List<UserItemOfficeGroupData> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<UserItemOfficeGroupData> groupList) {
        this.groupList = groupList;
    }
}
