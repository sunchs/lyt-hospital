package com.sunchs.lyt.report.bean;

import java.util.List;

public class TempItemOfficeSettingParam {

    private int itemId;
    private List<TempItemOfficeSettingOfficeBean> valueList;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public List<TempItemOfficeSettingOfficeBean> getValueList() {
        return valueList;
    }

    public void setValueList(List<TempItemOfficeSettingOfficeBean> valueList) {
        this.valueList = valueList;
    }
}
