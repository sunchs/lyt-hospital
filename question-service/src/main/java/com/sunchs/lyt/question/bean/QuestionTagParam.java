package com.sunchs.lyt.question.bean;

import java.util.List;

public class QuestionTagParam {

    public Integer id;
    public Integer pid;
    public String title;
    public Integer status;
    public String remarks;
    public List<QuestionTagParam> children;

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

    public String getRemarks() {
        return remarks;
    }

    public List<QuestionTagParam> getChildren() {
        return children;
    }
}
