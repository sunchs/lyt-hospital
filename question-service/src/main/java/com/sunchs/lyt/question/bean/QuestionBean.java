package com.sunchs.lyt.question.bean;

import java.util.List;

public class QuestionBean {

    /**
     * 起跳题目ID
     */
    public int questionId;

    /**
     * 跳转模式：0、不跳转， 1、题目跳转，2、选项跳转
     */
    public int skipMode;

    /**
     * 跳转内容
     * skipQuestionId、skipContent 二选一
     */
    public int skipQuestionId;
    public List<OptionSkipParam> skipContent;

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getSkipMode() {
        return skipMode;
    }

    public void setSkipMode(int skipMode) {
        this.skipMode = skipMode;
    }

    public int getSkipQuestionId() {
        return skipQuestionId;
    }

    public void setSkipQuestionId(int skipQuestionId) {
        this.skipQuestionId = skipQuestionId;
    }

    public List<OptionSkipParam> getSkipContent() {
        return skipContent;
    }

    public void setSkipContent(List<OptionSkipParam> skipContent) {
        this.skipContent = skipContent;
    }
}
