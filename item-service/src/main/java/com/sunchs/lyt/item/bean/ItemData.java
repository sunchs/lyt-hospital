package com.sunchs.lyt.item.bean;

import com.sunchs.lyt.db.business.entity.Item;
import com.sunchs.lyt.db.business.entity.Questionnaire;

import java.util.Date;
import java.util.List;

public class ItemData extends Item {

    private String startTimeName;

    private String endTimeName;

    private String statusName;

    private String finishTimeName;

    private String createTimeName;

    private String approachTimeName;
    private String deliveryTimeName;
    private String dataAnalysisTimeName;
    private String reportStartTimeName;
    private String reportEndTimeName;

    private List<Questionnaire> questionnaireList;

    public String getStartTimeName() {
        return startTimeName;
    }

    public void setStartTimeName(String startTimeName) {
        this.startTimeName = startTimeName;
    }

    public String getEndTimeName() {
        return endTimeName;
    }

    public void setEndTimeName(String endTimeName) {
        this.endTimeName = endTimeName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getFinishTimeName() {
        return finishTimeName;
    }

    public void setFinishTimeName(String finishTimeName) {
        this.finishTimeName = finishTimeName;
    }

    public String getCreateTimeName() {
        return createTimeName;
    }

    public void setCreateTimeName(String createTimeName) {
        this.createTimeName = createTimeName;
    }

    public List<Questionnaire> getQuestionnaireList() {
        return questionnaireList;
    }

    public void setQuestionnaireList(List<Questionnaire> questionnaireList) {
        this.questionnaireList = questionnaireList;
    }

    public String getApproachTimeName() {
        return approachTimeName;
    }

    public void setApproachTimeName(String approachTimeName) {
        this.approachTimeName = approachTimeName;
    }

    public String getDeliveryTimeName() {
        return deliveryTimeName;
    }

    public void setDeliveryTimeName(String deliveryTimeName) {
        this.deliveryTimeName = deliveryTimeName;
    }

    public String getDataAnalysisTimeName() {
        return dataAnalysisTimeName;
    }

    public void setDataAnalysisTimeName(String dataAnalysisTimeName) {
        this.dataAnalysisTimeName = dataAnalysisTimeName;
    }

    public String getReportStartTimeName() {
        return reportStartTimeName;
    }

    public void setReportStartTimeName(String reportStartTimeName) {
        this.reportStartTimeName = reportStartTimeName;
    }

    public String getReportEndTimeName() {
        return reportEndTimeName;
    }

    public void setReportEndTimeName(String reportEndTimeName) {
        this.reportEndTimeName = reportEndTimeName;
    }
}
