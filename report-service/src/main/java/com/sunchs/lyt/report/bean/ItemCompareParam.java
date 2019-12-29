package com.sunchs.lyt.report.bean;

import java.util.List;

public class ItemCompareParam {

    private int itemId;
    private int officeType;
    private int targetThree;
    private String startTime;
    private String endTime;

    private List<ItemCompareBean> valueList;

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

    public int getTargetThree() {
        return targetThree;
    }

    public void setTargetThree(int targetThree) {
        this.targetThree = targetThree;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<ItemCompareBean> getValueList() {
        return valueList;
    }

    public void setValueList(List<ItemCompareBean> valueList) {
        this.valueList = valueList;
    }
}
