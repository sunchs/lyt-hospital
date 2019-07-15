package com.sunchs.lyt.question.bean;

import java.util.List;

public class QuestionTargetParam {

    public int id;
    public int pid;
    public String title;
    public int status;
    public String remarks;
    public List<QuestionTargetParam> children;

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

    public List<QuestionTargetParam> getChildren() {
        return children;
    }
}