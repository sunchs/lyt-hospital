package com.sunchs.lyt.report.bean;

import java.util.List;

public class SingleOfficeSatisfyData {

    private Double officeSatisfyValue;
    private Integer answerQuantity;
    private Integer levelValue;
    private String officeName;
    private List<SingleOfficeData> questionList;

    public Double getOfficeSatisfyValue() {
        return officeSatisfyValue;
    }

    public void setOfficeSatisfyValue(Double officeSatisfyValue) {
        this.officeSatisfyValue = officeSatisfyValue;
    }

    public Integer getAnswerQuantity() {
        return answerQuantity;
    }

    public void setAnswerQuantity(Integer answerQuantity) {
        this.answerQuantity = answerQuantity;
    }

    public Integer getLevelValue() {
        return levelValue;
    }

    public void setLevelValue(Integer levelValue) {
        this.levelValue = levelValue;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public List<SingleOfficeData> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<SingleOfficeData> questionList) {
        this.questionList = questionList;
    }
}
