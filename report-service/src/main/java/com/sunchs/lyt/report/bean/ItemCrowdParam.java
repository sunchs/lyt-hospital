package com.sunchs.lyt.report.bean;

import java.util.List;

public class ItemCrowdParam {

    private int itemId;
    private int officeType;
    private List<Integer> optionIds;
    private String startTime;
    private String endTime;

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

    public List<Integer> getOptionIds() {
        return optionIds;
    }

    public void setOptionIds(List<Integer> optionIds) {
        this.optionIds = optionIds;
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
}
