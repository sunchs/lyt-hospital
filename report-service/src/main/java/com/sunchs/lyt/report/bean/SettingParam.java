package com.sunchs.lyt.report.bean;

import java.util.List;

public class SettingParam {

    private int itemId;
    private List<Integer> officeList;
    private List<Integer> targetList;
    private int position;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
