package com.sunchs.lyt.report.bean;

import com.sunchs.lyt.framework.bean.TitleData;

import java.util.List;

public class SingleOfficeSatisfyData {

    private Double officeSatisfyValue;
    private Integer answerQuantity;
    private Integer levelValue;
    private List<TitleData> titleList;
    private List<TitleValueListVO> valueList;

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

    public List<TitleData> getTitleList() {
        return titleList;
    }

    public void setTitleList(List<TitleData> titleList) {
        this.titleList = titleList;
    }

    public List<TitleValueListVO> getValueList() {
        return valueList;
    }

    public void setValueList(List<TitleValueListVO> valueList) {
        this.valueList = valueList;
    }
}
