package com.sunchs.lyt.item.bean;

import java.util.List;

public class OfficeGroupData {

    private Integer id;
    private String title;
    private List<OfficeGroupBean> officeList;

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

    public List<OfficeGroupBean> getOfficeList() {
        return officeList;
    }

    public void setOfficeList(List<OfficeGroupBean> officeList) {
        this.officeList = officeList;
    }
}
