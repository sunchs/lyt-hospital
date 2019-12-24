package com.sunchs.lyt.report.bean;

import java.util.List;

public class ItemCompareData {

    private Integer id;
    private String title;
    private List<ItemCompareValue> valueList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ItemCompareValue> getValueList() {
        return valueList;
    }

    public void setValueList(List<ItemCompareValue> valueList) {
        this.valueList = valueList;
    }
}
