package com.sunchs.lyt.question.bean;

import java.util.List;

public class QuestionData {
    public Integer id;
    public String title;
    public Integer status;
    public String remark;
    public TargetData target;
    public Integer optionType;
    public List<OptionData> option;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public TargetData getTarget() {
        return target;
    }

    public void setTarget(TargetData target) {
        this.target = target;
    }

    public Integer getOptionType() {
        return optionType;
    }

    public void setOptionType(Integer optionType) {
        this.optionType = optionType;
    }

    public List<OptionData> getOption() {
        return option;
    }

    public void setOption(List<OptionData> option) {
        this.option = option;
    }
}
