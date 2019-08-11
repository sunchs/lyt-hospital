package com.sunchs.lyt.item.bean;

public class SyncAnswerOptionParam {

    /**
     * 问题ID
     */
    private Integer questionId;

    /**
     * 问题名称
     */
    private String questionName;

    /**
     * 选项ID
     */
    private Integer optionId;

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

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
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
}
