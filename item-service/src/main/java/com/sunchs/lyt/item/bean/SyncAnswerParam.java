package com.sunchs.lyt.item.bean;

import java.util.List;

public class SyncAnswerParam {

    /**
     * 答卷人ID
     */
    private int memberId;

    /**
     * 项目ID
     */
    private int itemId;

    /**
     * 科室ID
     */
    private int officeId;

    /**
     * 问卷ID
     */
    private int questionnaireId;

    /**
     * 患者编号
     */
    private String patientNumber;

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

    /**
     * 答卷图片列表
     */
    private List<AnswerImageData> imageList;

    /**
     * 答卷答案列表
     */
    private List<SyncAnswerOptionParam> questionList;


    public int getMemberId() {
        return memberId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getOfficeId() {
        return officeId;
    }

    public int getQuestionnaireId() {
        return questionnaireId;
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getTimeDuration() {
        return timeDuration;
    }

    public List<AnswerImageData> getImageList() {
        return imageList;
    }

    public List<SyncAnswerOptionParam> getQuestionList() {
        return questionList;
    }
}
