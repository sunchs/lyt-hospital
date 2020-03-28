package com.sunchs.lyt.report.bean;

import java.util.List;

public class TempItemOfficeSettingOfficeBean {

    private Integer officeId;
    private List<Integer> targetList;

    public Integer getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    public List<Integer> getTargetList() {
        return targetList;
    }

    public void setTargetList(List<Integer> targetList) {
        this.targetList = targetList;
    }
}
