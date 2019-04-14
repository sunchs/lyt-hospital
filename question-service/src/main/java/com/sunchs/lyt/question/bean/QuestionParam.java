package com.sunchs.lyt.question.bean;

import com.sunchs.lyt.framework.bean.PagingParam;

import java.util.List;

public class QuestionParam extends PagingParam {

    public int id;
    public String title;
    public Integer status;
    public String remark;
    public TargetParam target;
    public OptionParam option;
    public List<TagParam> tagList;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getStatus() {
        return status;
    }

    public String getRemark() {
        return remark;
    }

    public TargetParam getTarget() {
        return target;
    }

    public OptionParam getOption() {
        return option;
    }

    public List<TagParam> getTagList() {
        return tagList;
    }
}
