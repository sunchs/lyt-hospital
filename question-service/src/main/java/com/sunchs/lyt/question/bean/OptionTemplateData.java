package com.sunchs.lyt.question.bean;

import java.util.List;

public class OptionTemplateData {

    private int id;
    private int pid;
    private List<OptionTemplateOptionParam> optionList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public List<OptionTemplateOptionParam> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<OptionTemplateOptionParam> optionList) {
        this.optionList = optionList;
    }
}
