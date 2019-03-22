package com.sunchs.lyt.question.bean;

import java.util.List;

public class QuestionParam {

    public Integer id;
    public String title;
    public Integer score;
    public Integer status;
    public String remark;
    public TargetParam target;
    public List<AttributeParam> attribute;
    public Integer optionType;
    public List<OptionParam> option;

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getScore() {
        return score;
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

    public List<AttributeParam> getAttribute() {
        return attribute;
    }

    public Integer getOptionType() {
        return optionType;
    }

    public List<OptionParam> getOption() {
        return option;
    }
}
