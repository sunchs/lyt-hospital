package com.sunchs.lyt.report.bean;

import com.sunchs.lyt.db.business.entity.SettingItemWeight;

import java.util.List;

public class ItemAllSatisfySettingParam {

    private int itemId;
    private int officeType;
    private List<SettingItemWeight> valueList;

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

    public List<SettingItemWeight> getValueList() {
        return valueList;
    }

    public void setValueList(List<SettingItemWeight> valueList) {
        this.valueList = valueList;
    }
}
