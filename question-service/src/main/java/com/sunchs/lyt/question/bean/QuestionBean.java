package com.sunchs.lyt.question.bean;

import java.util.List;

public class QuestionBean {

    public int questionId;
    public String skipContent;
    public List<TagParam> tagList;

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

    public List<TagParam> getTagList() {
        return tagList;
    }

    public void setTagList(List<TagParam> tagList) {
        this.tagList = tagList;
    }
}
