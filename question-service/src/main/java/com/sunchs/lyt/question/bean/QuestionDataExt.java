package com.sunchs.lyt.question.bean;

import java.util.List;

public class QuestionDataExt extends QuestionData {

    private int skipMode;
    private int skipQuestionId;
    private List<OptionData> skipContent;

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

    public List<OptionData> getSkipContent() {
        return skipContent;
    }

    public void setSkipContent(List<OptionData> skipContent) {
        this.skipContent = skipContent;
    }
}
