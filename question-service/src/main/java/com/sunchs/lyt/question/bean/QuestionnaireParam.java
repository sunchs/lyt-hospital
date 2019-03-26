package com.sunchs.lyt.question.bean;

import com.sunchs.lyt.framework.bean.PagingParam;

public class QuestionnaireParam extends PagingParam {

    public int id;
    public String title;
    public Integer status;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getStatus() {
        return status;
    }
}
