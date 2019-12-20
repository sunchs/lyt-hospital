package com.sunchs.lyt.report.bean;

import java.util.List;

public class CustomItemOfficeSettingParam {

    private int itemId;

    private List<CustomItemOfficeSettingOfficeBean> customList;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public List<CustomItemOfficeSettingOfficeBean> getCustomList() {
        return customList;
    }

    public void setCustomList(List<CustomItemOfficeSettingOfficeBean> customList) {
        this.customList = customList;
    }
}
