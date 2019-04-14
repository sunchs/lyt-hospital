package com.sunchs.lyt.question.bean;

import java.util.List;

public class QuestionTargetData {
    public int id;
    public int pid;
    public String title;
    public int status;
    public int sort;
    public String remarks;
    public String updateTime;
    public List<QuestionTargetData> children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<QuestionTargetData> getChildren() {
        return children;
    }

    public void setChildren(List<QuestionTargetData> children) {
        this.children = children;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
