package com.sunchs.lyt.report.bean;

import java.util.List;

public class TitleChildrenVO {

    private Integer id;
    private String title;
    private List<TitleChildrenVO> children;

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

    public List<TitleChildrenVO> getChildren() {
        return children;
    }

    public void setChildren(List<TitleChildrenVO> children) {
        this.children = children;
    }
}
