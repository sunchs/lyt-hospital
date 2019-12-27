package com.sunchs.lyt.report.bean;

import com.sunchs.lyt.db.business.entity.ReportAnswerOption;

import java.util.List;

public class ItemCompareBean {

    private int itemId;
    private int officeType;
    private String startTime;
    private String endTime;

    /**
     * 临时数据
     */
    private List<ReportAnswerOption> tempOptionList;

    private int colIndex;

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

    public List<ReportAnswerOption> getTempOptionList() {
        return tempOptionList;
    }

    public void setTempOptionList(List<ReportAnswerOption> tempOptionList) {
        this.tempOptionList = tempOptionList;
    }

    public int getColIndex() {
        return colIndex;
    }

    public void setColIndex(int colIndex) {
        this.colIndex = colIndex;
    }
}
