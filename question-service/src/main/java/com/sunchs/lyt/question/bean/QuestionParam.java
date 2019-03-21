package com.sunchs.lyt.question.bean;

import java.util.List;

public class QuestionParam {

    public Integer id;
    public String title;
    public Integer score;
    public Integer status;
    public String remark;
    public TargetParam target;
    public List<AttributeParam> attribute;
    public Integer optionType;
    public List<OptionParam> option;
}
