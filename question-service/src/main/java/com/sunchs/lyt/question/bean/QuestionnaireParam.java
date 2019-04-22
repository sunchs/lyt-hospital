package com.sunchs.lyt.question.bean;

import com.sunchs.lyt.framework.bean.PagingParam;

import java.util.List;

public class QuestionnaireParam extends PagingParam {

    public int id;
    public String title;
    public Integer status;
    public int targetOne;
    public List<QuestionBean> question;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getStatus() {
        return status;
    }

    public int getTargetOne() {
        return targetOne;
    }

    public List<QuestionBean> getQuestion() {
        return question;
    }
}
