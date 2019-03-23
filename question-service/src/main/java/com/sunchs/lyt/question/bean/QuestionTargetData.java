package com.sunchs.lyt.question.bean;

import java.util.List;

public class QuestionTargetData {
    public Integer id;
    public Integer pid;
    public String title;
    public Integer status;
    public Integer sort;
    public String remark;
    public List<QuestionTargetData> children;

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

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setChildren(List<QuestionTargetData> children) {
        this.children = children;
    }
}
