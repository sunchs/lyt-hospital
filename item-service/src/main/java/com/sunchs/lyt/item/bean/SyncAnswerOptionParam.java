package com.sunchs.lyt.item.bean;

import java.util.List;

public class SyncAnswerOptionParam {

    /**
     * 问题ID
     */
    private int questionId;

    /**
     * 问题名称
     */
    private String questionName;

    /**
     * 选项类型
     */
    private String optionMode;

    /**
     * 选项ID集合
     */
    private List<Integer> optionIds;

    /**
     * 选项输入内容
     */
    private String optionValue;

    /**
     * 选项名称
     */
    private String optionName;

    /**
     * 开始答卷时间
     */
    private String startTime;

    /**
     * 结束答卷时间
     */
    private String endTime;

    /**
     * 持续时间
     */
    private int timeDuration;


    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getOptionMode() {
        return optionMode;
    }

    public void setOptionMode(String optionMode) {
        this.optionMode = optionMode;
    }

    public List<Integer> getOptionIds() {
        return optionIds;
    }

    public void setOptionIds(List<Integer> optionIds) {
        this.optionIds = optionIds;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(int timeDuration) {
        this.timeDuration = timeDuration;
    }

    @Override
    public String toString() {
        return "SyncAnswerOptionParam{" +
                "questionId=" + questionId +
                ", questionName='" + questionName + '\'' +
                ", optionMode='" + optionMode + '\'' +
                ", optionIds=" + optionIds +
                ", optionValue='" + optionValue + '\'' +
                ", optionName='" + optionName + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", timeDuration=" + timeDuration +
                '}';
    }
}
