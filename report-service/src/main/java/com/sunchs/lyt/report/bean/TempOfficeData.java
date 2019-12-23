package com.sunchs.lyt.report.bean;

import com.sunchs.lyt.framework.bean.TitleData;

import java.util.List;

public class TempOfficeData {

    private Integer id;
    private List<TitleData> officeList;
    private List<TitleData> targetList;
    private List<OfficeTargetSatisfyData> satisfyList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<TitleData> getOfficeList() {
        return officeList;
    }

    public void setOfficeList(List<TitleData> officeList) {
        this.officeList = officeList;
    }

    public List<TitleData> getTargetList() {
        return targetList;
    }

    public void setTargetList(List<TitleData> targetList) {
        this.targetList = targetList;
    }

    public List<OfficeTargetSatisfyData> getSatisfyList() {
        return satisfyList;
    }

    public void setSatisfyList(List<OfficeTargetSatisfyData> satisfyList) {
        this.satisfyList = satisfyList;
    }
}
