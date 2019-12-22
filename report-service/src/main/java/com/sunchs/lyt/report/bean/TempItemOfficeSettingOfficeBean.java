package com.sunchs.lyt.report.bean;

import java.util.List;

public class TempItemOfficeSettingOfficeBean {

    private int officeType;
    private List<Integer> officeList;
    private List<Integer> targetList;

    public int getOfficeType() {
        return officeType;
    }

    public void setOfficeType(int officeType) {
        this.officeType = officeType;
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
