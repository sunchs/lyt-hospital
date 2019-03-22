package com.sunchs.lyt.question.bean;

import java.util.List;

public class QuestionAttributeData {

    public Integer id;
    public Integer pid;
    public String title;
    public Integer status;
    public Integer sort;
    public List<QuestionAttributeData> children;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setChildren(List<QuestionAttributeData> children) {
        this.children = children;
    }
}
