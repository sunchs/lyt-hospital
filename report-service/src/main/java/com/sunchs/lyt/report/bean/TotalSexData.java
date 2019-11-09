package com.sunchs.lyt.report.bean;

public class TotalSexData {

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
     * 已答数里
     */
    private Integer quantity;

    /**
     * 比率
     */
    private Double rate;

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
