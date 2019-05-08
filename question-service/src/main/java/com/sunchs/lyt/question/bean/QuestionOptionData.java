package com.sunchs.lyt.question.bean;

import java.util.List;

public class QuestionOptionData {

    public int id;
    public String title;
    public String remarks;
    public String mode;
    public String updateTimeName;
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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getUpdateTimeName() {
        return updateTimeName;
    }

    public void setUpdateTimeName(String updateTimeName) {
        this.updateTimeName = updateTimeName;
    }

    public List<OptionBean> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<OptionBean> optionList) {
        this.optionList = optionList;
    }
}
