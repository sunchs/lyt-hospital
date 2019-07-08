package com.sunchs.lyt.question.bean;

public class QuestionDataExt extends QuestionData {

    private int skipMode;
    private int skipQuestionId;

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
}
