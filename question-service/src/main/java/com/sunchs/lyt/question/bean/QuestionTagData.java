package com.sunchs.lyt.question.bean;

import java.util.List;

public class QuestionTagData {

    public Integer id;
    public Integer pid;
    public String title;
    public Integer status;
    public String remarks;
    public String updateTime;
    public List<QuestionTagData> children;

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

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setChildren(List<QuestionTagData> children) {
        this.children = children;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
