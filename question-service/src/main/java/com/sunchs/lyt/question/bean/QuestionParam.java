package com.sunchs.lyt.question.bean;

import com.sunchs.lyt.framework.bean.PagingParam;

import java.util.List;

public class QuestionParam extends PagingParam {

    public Integer id;
    public String title;
    public Integer status;
    public String remark;
    public TargetParam target;
    public Integer optionType;
    public List<OptionParam> option;

    public Integer getId() {
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

    public Integer getOptionType() {
        return optionType;
    }

    public List<OptionParam> getOption() {
        return option;
    }
}
