package com.sunchs.lyt.report.bean;

import java.util.List;

public class TitleValueListVO extends TitleValueDataVO {

    private List<SingleOfficeForQuestionList> questionList;

    public List<SingleOfficeForQuestionList> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<SingleOfficeForQuestionList> questionList) {
        this.questionList = questionList;
    }
}
