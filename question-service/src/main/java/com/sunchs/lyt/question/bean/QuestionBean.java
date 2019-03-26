package com.sunchs.lyt.question.bean;

import java.util.List;

public class QuestionBean {

    public int questionId;
    public String skipContent;
    public List<AttributeParam> attribute;

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getSkipContent() {
        return skipContent;
    }

    public void setSkipContent(String skipContent) {
        this.skipContent = skipContent;
    }

    public List<AttributeParam> getAttribute() {
        return attribute;
    }

    public void setAttribute(List<AttributeParam> attribute) {
        this.attribute = attribute;
    }
}
