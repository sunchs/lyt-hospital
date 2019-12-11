package com.sunchs.lyt.report.bean;

import java.util.List;

public class TotalParam {

    private int itemId;
    private int tagId;
    private int targetId;
    private int targetOne;
    private int position;
    private List<Integer> officeList;
    private List<Integer> targetList;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getTargetOne() {
        return targetOne;
    }

    public void setTargetOne(int targetOne) {
        this.targetOne = targetOne;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<Integer> getOfficeList() {
        return officeList;
    }

    public void setOfficeList(List<Integer> officeList) {
        this.officeList = officeList;
    }

    public List<Integer> getTargetList() {
        return targetList;
    }

    public void setTargetList(List<Integer> targetList) {
        this.targetList = targetList;
    }
}
