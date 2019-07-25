package com.sunchs.lyt.question.bean;

import com.sunchs.lyt.framework.bean.PagingParam;

import java.util.List;

public class QuestionnaireParam extends PagingParam {

    private int id;
    private int hospitalId;
    private String title;
    private int status;
    private int targetOne;
    private List<QuestionBean> questionList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
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

    public int getTargetOne() {
        return targetOne;
    }

    public void setTargetOne(int targetOne) {
        this.targetOne = targetOne;
    }

    public List<QuestionBean> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<QuestionBean> questionList) {
        this.questionList = questionList;
    }
}
