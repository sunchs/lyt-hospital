package com.sunchs.lyt.question.bean;

import java.util.List;

public class QuestionData {
    public int id;
    public String title;
    public int status;
    public String statusName;
    public String remark;
    public TargetData target;
    public int optionType;
    public String optionMode;
    public String optionTypeName;
    public List<OptionData> option;
    public List<TagData> tagList;
    public String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
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

    public int getOptionType() {
        return optionType;
    }

    public void setOptionType(int optionType) {
        this.optionType = optionType;
    }

    public String getOptionMode() {
        return optionMode;
    }

    public void setOptionMode(String optionMode) {
        this.optionMode = optionMode;
    }

    public String getOptionTypeName() {
        return optionTypeName;
    }

    public void setOptionTypeName(String optionTypeName) {
        this.optionTypeName = optionTypeName;
    }

    public List<OptionData> getOption() {
        return option;
    }

    public void setOption(List<OptionData> option) {
        this.option = option;
    }

    public List<TagData> getTagList() {
        return tagList;
    }

    public void setTagList(List<TagData> tagList) {
        this.tagList = tagList;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
