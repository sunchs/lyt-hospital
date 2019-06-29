package com.sunchs.lyt.question.bean;

import java.util.List;

public class QuestionBean {

    /**
     * 起跳题目ID
     */
    private int questionId;

    /**
     * 跳转模式：0、不跳转， 1、题目跳转，2、选项跳转
     */
    private int skipMode;

    /**
     * 跳转内容
     * skipQuestionId、skipContent 二选一
     */
    private int skipQuestionId;
    private List<OptionSkipParam> skipContent;

    /**
     * 排序
     */
    private int sort;

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

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
