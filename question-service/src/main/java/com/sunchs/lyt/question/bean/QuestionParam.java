package com.sunchs.lyt.question.bean;

import com.sunchs.lyt.framework.bean.PagingParam;

import java.util.List;

public class QuestionParam extends PagingParam {

    private int id;
    private int hospitalId;
    private String number;
    private int isPublic;
    private String title;
    private int status;
    private int targetOne;
    private int targetTwo;
    private int targetThree;
    private int optionId;
    private int optionMaxQuantity;
    private int isUseFace;
    private String remark;
    private List<TagParam> tagList;

    public int getId() {
        return id;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public String getNumber() {
        return number;
    }

    public int getIsPublic() {
        return isPublic;
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

    public int getOptionMaxQuantity() {
        return optionMaxQuantity;
    }

    public String getRemark() {
        return remark;
    }

    public List<TagParam> getTagList() {
        return tagList;
    }

    public int getIsUseFace() {
        return isUseFace;
    }
}
