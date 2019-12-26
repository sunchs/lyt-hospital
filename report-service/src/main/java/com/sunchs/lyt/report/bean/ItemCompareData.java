package com.sunchs.lyt.report.bean;

import com.sunchs.lyt.framework.bean.IdTitleData;

import java.util.List;

public class ItemCompareData {

    /**
     * 总体满意度列表
     */
    private List<SatisfyData> allSatisfyList;

    /**
     * 列
     */
    private List<IdTitleData> colList;
    /**
     * 行
     */
    private List<IdTitleData> rowList;
    /**
     * 值
     */
    private List<ItemCompareValue> valueList;


    public List<SatisfyData> getAllSatisfyList() {
        return allSatisfyList;
    }

    public void setAllSatisfyList(List<SatisfyData> allSatisfyList) {
        this.allSatisfyList = allSatisfyList;
    }

    public List<IdTitleData> getColList() {
        return colList;
    }

    public void setColList(List<IdTitleData> colList) {
        this.colList = colList;
    }

    public List<IdTitleData> getRowList() {
        return rowList;
    }

    public void setRowList(List<IdTitleData> rowList) {
        this.rowList = rowList;
    }

    public List<ItemCompareValue> getValueList() {
        return valueList;
    }

    public void setValueList(List<ItemCompareValue> valueList) {
        this.valueList = valueList;
    }
}
