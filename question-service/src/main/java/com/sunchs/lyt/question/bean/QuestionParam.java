package com.sunchs.lyt.question.bean;

import com.sunchs.lyt.framework.bean.PagingParam;

import java.util.List;

public class QuestionParam extends PagingParam {

    public int id;
    public String number;
    public String title;
    public int status;
    public int targetOne;
    public int targetTwo;
    public int targetThree;
    public int optionId;
    public String remark;
    public List<TagParam> tagList;

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public int getStatus() {
        return status;
    }

    public int getTargetOne() {
        return targetOne;
    }

    public int getTargetTwo() {
        return targetTwo;
    }

    public int getTargetThree() {
        return targetThree;
    }

    public int getOptionId() {
        return optionId;
    }

    public String getRemark() {
        return remark;
    }

    public List<TagParam> getTagList() {
        return tagList;
    }
}
