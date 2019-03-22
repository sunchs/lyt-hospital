package com.sunchs.lyt.question.bean;

import java.util.List;

public class QuestionAttributeParam {

    public Integer id;
    public Integer pid;
    public String title;
    public Integer status;
    public String sort;
    public List<QuestionAttributeParam> children;

    public Integer getId() {
        return id;
    }

    public Integer getPid() {
        return pid;
    }

    public String getTitle() {
        return title;
    }

    public Integer getStatus() {
        return status;
    }

    public String getSort() {
        return sort;
    }

    public List<QuestionAttributeParam> getChildren() {
        return children;
    }
}
