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

    private int skipMode;
    private int skipQuestionId;
    private List<OptionData> optionList;
    private String optionListName;
    private List<TagData> tagList;
    private String tagListName;

    private int sort;

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

    public int getSkipMode() {
        return skipMode;
    }

    public void setSkipMode(int skipMode) {
        this.skipMode = skipMode;
    }

    public int getSkipQuestionId() {
        return skipQuestionId;
    }

    public void setSkipQuestionId(int skipQuestionId) {
        this.skipQuestionId = skipQuestionId;
    }

    public List<OptionData> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<OptionData> optionList) {
        this.optionList = optionList;
    }

    public String getOptionListName() {
        return optionListName;
    }

    public void setOptionListName(String optionListName) {
        this.optionListName = optionListName;
    }

    public List<TagData> getTagList() {
        return tagList;
    }

    public void setTagList(List<TagData> tagList) {
        this.tagList = tagList;
    }

    public String getTagListName() {
        return tagListName;
    }

    public void setTagListName(String tagListName) {
        this.tagListName = tagListName;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
