package com.sunchs.lyt.report.bean;

import java.util.List;

public class AnswerQuestionData {

//    /**
//     * 答案ID
//     */
//    private Integer answerId;

    /**
     * 题目ID
     */
    private Integer questionId;

    /**
     * 题目名称
     */
    private String questionName;

    /**
     * 题目数量
     */
    private int questionQuantity;

    /**
     * 题目占比例
     */
    private double questionRateValue;

    /**
     * 选项列表
     */
    private List<AnswerQuestionOptionData> optionList;

//
//    public Integer getAnswerId() {
//        return answerId;
//    }
//
//    public void setAnswerId(Integer answerId) {
//        this.answerId = answerId;
//    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public List<AnswerQuestionOptionData> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<AnswerQuestionOptionData> optionList) {
        this.optionList = optionList;
    }

    public int getQuestionQuantity() {
        return questionQuantity;
    }

    public void setQuestionQuantity(int questionQuantity) {
        this.questionQuantity = questionQuantity;
    }

    public double getQuestionRateValue() {
        return questionRateValue;
    }

    public void setQuestionRateValue(double questionRateValue) {
        this.questionRateValue = questionRateValue;
    }

    @Override
    public String toString() {
        return "AnswerQuestionData{" +
//                "answerId=" + answerId +
                ", questionId=" + questionId +
                ", questionName='" + questionName + '\'' +
                ", questionQuantity=" + questionQuantity +
                ", questionRateValue=" + questionRateValue +
                ", optionList=" + optionList +
                '}';
    }
}
