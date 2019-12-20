package com.sunchs.lyt.report.bean;

import java.util.List;

public class CustomOfficeData {

    private Integer id;
    private String title;
    private List<CustomOfficeTargetData> targetList;

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

    public List<CustomOfficeTargetData> getTargetList() {
        return targetList;
    }

    public void setTargetList(List<CustomOfficeTargetData> targetList) {
        this.targetList = targetList;
    }
}
