package com.sunchs.lyt.report.bean;

import java.util.List;

public class CustomItemOfficeSettingOfficeBean {

    private int officeType;
    private String title;
    private int questionnaireId;
    private int questionId;
    private int optionId;
    private List<TargetBean> targetList;

    public int getOfficeType() {
        return officeType;
    }

    public void setOfficeType(int officeType) {
        this.officeType = officeType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(int questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public List<TargetBean> getTargetList() {
        return targetList;
    }

    public void setTargetList(List<TargetBean> targetList) {
        this.targetList = targetList;
    }
}
