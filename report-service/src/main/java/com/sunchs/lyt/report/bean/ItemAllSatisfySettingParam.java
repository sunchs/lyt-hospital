package com.sunchs.lyt.report.bean;

import java.util.List;

public class ItemAllSatisfySettingParam {

    private int itemId;
    private List<ItemAllSatisfySettingBean> valueList;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public List<ItemAllSatisfySettingBean> getValueList() {
        return valueList;
    }

    public void setValueList(List<ItemAllSatisfySettingBean> valueList) {
        this.valueList = valueList;
    }
}
