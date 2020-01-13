package com.sunchs.lyt.report.bean;

import java.util.List;

public class OutputParam {

    private int itemId;
    private int officeId;
    private int tagId;
    private int targetOne;
    private String startTime;
    private String endTime;
    private int officeType;
    private List<Integer> officeIds;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getOfficeId() {
        return officeId;
    }

    public void setOfficeId(int officeId) {
        this.officeId = officeId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getTargetOne() {
        return targetOne;
    }

    public void setTargetOne(int targetOne) {
        this.targetOne = targetOne;
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
