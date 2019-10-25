package com.sunchs.lyt.report.bean;

public class AnswerQuestionOptionData {

    /**
     * 选项ID
     */
    private Integer optionId;

    /**
     * 选项名称
     */
    private String optionName;

    /**
     * 选项数量
     */
    private int optionQuantity;

    /**
     * 选项占比例
     */
    private double optionRateValue;

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

    public int getOptionQuantity() {
        return optionQuantity;
    }

    public void setOptionQuantity(int optionQuantity) {
        this.optionQuantity = optionQuantity;
    }

    public double getOptionRateValue() {
        return optionRateValue;
    }

    public void setOptionRateValue(double optionRateValue) {
        this.optionRateValue = optionRateValue;
    }
}
