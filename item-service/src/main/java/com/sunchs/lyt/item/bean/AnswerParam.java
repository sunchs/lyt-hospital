package com.sunchs.lyt.item.bean;

import com.sunchs.lyt.framework.bean.PagingList;

public class AnswerParam extends PagingList {

    /**
     * 自增ID
     */
    private int id;

    /**
     * 医院ID
     */
    private int hospitalId;

    /**
     * 项目ID
     */
    private int itemId;

    /**
     * 科室ID，取项目科室ID
     */
    private int officeId;

    /**
     * 问卷ID
     */
    private int questionnaireId;

    /**
     * 负责人ID
     */
    private int userId;

    /**
     * 患者编号
     */
    private String patientNumber;

    /**
     * 状态
     */
    private int status;

    /**
     * 处理原因
     */
    private String reason;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getOfficeId() {
        return officeId;
    }

    public void setOfficeId(int officeId) {
        this.officeId = officeId;
    }

    public int getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(int questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
