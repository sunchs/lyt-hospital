package com.sunchs.lyt.report.bean;

public class SingleOfficeData {

    private Integer questionId;
    private String questionName;
    private Double value1;
    private Double value2;
    private Double value3;
    private Double value4;
    private Double value5;
    private Double countValue;
    private Double questionSatisfyValue;
    private Double hospitalSatisfyValue;
    private Integer questionLevel;

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

    public Double getValue1() {
        return value1;
    }

    public void setValue1(Double value1) {
        this.value1 = value1;
    }

    public Double getValue2() {
        return value2;
    }

    public void setValue2(Double value2) {
        this.value2 = value2;
    }

    public Double getValue3() {
        return value3;
    }

    public void setValue3(Double value3) {
        this.value3 = value3;
    }

    public Double getValue4() {
        return value4;
    }

    public void setValue4(Double value4) {
        this.value4 = value4;
    }

    public Double getValue5() {
        return value5;
    }

    public void setValue5(Double value5) {
        this.value5 = value5;
    }

    public Double getCountValue() {
        return countValue;
    }

    public void setCountValue(Double countValue) {
        this.countValue = countValue;
    }

    public Double getQuestionSatisfyValue() {
        return questionSatisfyValue;
    }

    public void setQuestionSatisfyValue(Double questionSatisfyValue) {
        this.questionSatisfyValue = questionSatisfyValue;
    }

    public Double getHospitalSatisfyValue() {
        return hospitalSatisfyValue;
    }

    public void setHospitalSatisfyValue(Double hospitalSatisfyValue) {
        this.hospitalSatisfyValue = hospitalSatisfyValue;
    }

    public Integer getQuestionLevel() {
        return questionLevel;
    }

    public void setQuestionLevel(Integer questionLevel) {
        this.questionLevel = questionLevel;
    }
}
