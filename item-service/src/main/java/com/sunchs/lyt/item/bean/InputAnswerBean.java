package com.sunchs.lyt.item.bean;

import com.sunchs.lyt.db.business.entity.Question;
import com.sunchs.lyt.db.business.entity.QuestionOption;

import java.util.Date;
import java.util.Map;

public class InputAnswerBean {

    private String patientNo;
    private Date startTime;
    private Date endTime;
    private Question question;
    private Integer position;
    private String optionValue;
    private Map<String, QuestionOption> questionOptionMap;

    public String getPatientNo() {
        return patientNo;
    }

    public void setPatientNo(String patientNo) {
        this.patientNo = patientNo;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public Map<String, QuestionOption> getQuestionOptionMap() {
        return questionOptionMap;
    }

    public void setQuestionOptionMap(Map<String, QuestionOption> questionOptionMap) {
        this.questionOptionMap = questionOptionMap;
    }
}
