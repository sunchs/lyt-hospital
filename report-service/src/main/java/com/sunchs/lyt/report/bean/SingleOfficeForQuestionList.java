package com.sunchs.lyt.report.bean;

import java.util.List;

public class SingleOfficeForQuestionList {

    private Integer questionId;
    private List<SingleOfficeRowValue> optionList;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public List<SingleOfficeRowValue> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<SingleOfficeRowValue> optionList) {
        this.optionList = optionList;
    }
}
