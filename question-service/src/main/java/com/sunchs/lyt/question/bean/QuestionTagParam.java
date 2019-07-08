package com.sunchs.lyt.question.bean;

import java.util.List;

public class QuestionTagParam {

    public int id;
    public int pid;
    public String title;
    public int status;
    public String remarks;
    public List<QuestionTagParam> children;

    public int getId() {
        return id;
    }

    public int getPid() {
        return pid;
    }

    public String getTitle() {
        return title;
    }

    public int getStatus() {
        return status;
    }

    public String getRemarks() {
        return remarks;
    }

    public List<QuestionTagParam> getChildren() {
        return children;
    }
}
