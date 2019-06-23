package com.sunchs.lyt.question.bean;

import com.sunchs.lyt.db.business.entity.Question;

import java.util.List;

public class QuestionData extends Question {

    private String statusName;
    private String targetOneName;
    private String targetTwoName;
    private String targetThreeName;

    private String optionMode;
    private String optionTypeName;

    private String updateTimeName;

    private List<OptionData> option;
    private String optionName;
    private List<TagData> tagList;

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getTargetOneName() {
        return targetOneName;
    }

    public void setTargetOneName(String targetOneName) {
        this.targetOneName = targetOneName;
    }

    public String getTargetTwoName() {
        return targetTwoName;
    }

    public void setTargetTwoName(String targetTwoName) {
        this.targetTwoName = targetTwoName;
    }

    public String getTargetThreeName() {
        return targetThreeName;
    }

    public void setTargetThreeName(String targetThreeName) {
        this.targetThreeName = targetThreeName;
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

    public String getUpdateTimeName() {
        return updateTimeName;
    }

    public void setUpdateTimeName(String updateTimeName) {
        this.updateTimeName = updateTimeName;
    }

    public List<OptionData> getOption() {
        return option;
    }

    public void setOption(List<OptionData> option) {
        this.option = option;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public List<TagData> getTagList() {
        return tagList;
    }

    public void setTagList(List<TagData> tagList) {
        this.tagList = tagList;
    }
}
