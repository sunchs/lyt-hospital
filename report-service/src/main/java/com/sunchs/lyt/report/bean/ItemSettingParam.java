package com.sunchs.lyt.report.bean;

import java.util.List;

public class ItemSettingParam {

    private int itemId;

    private int officeType;

    private List<Integer> officeIds;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getOfficeType() {
        return officeType;
    }

    public void setOfficeType(int officeType) {
        this.officeType = officeType;
    }

    public List<Integer> getOfficeIds() {
        return officeIds;
    }

    public void setOfficeIds(List<Integer> officeIds) {
        this.officeIds = officeIds;
    }
}
