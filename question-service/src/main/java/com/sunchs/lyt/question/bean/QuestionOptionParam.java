package com.sunchs.lyt.question.bean;

import java.util.List;

public class QuestionOptionParam {

    public int id;
    public String remarks;
    public List<String> optionList;

    public int getId() {
        return id;
    }

    public String getRemarks() {
        return remarks;
    }

    public List<String> getOptionList() {
        return optionList;
    }
}
