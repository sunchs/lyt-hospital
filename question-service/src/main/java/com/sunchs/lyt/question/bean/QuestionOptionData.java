package com.sunchs.lyt.question.bean;

import java.util.List;

public class QuestionOptionData {

    public int id;
    public String title;
    public String remarks;
    public List<OptionBean> optionList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<OptionBean> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<OptionBean> optionList) {
        this.optionList = optionList;
    }
}
