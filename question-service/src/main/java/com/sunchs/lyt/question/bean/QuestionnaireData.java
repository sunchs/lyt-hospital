package com.sunchs.lyt.question.bean;

import com.sunchs.lyt.db.business.entity.Questionnaire;
import com.sunchs.lyt.framework.util.FormatUtil;
import com.sunchs.lyt.question.dao.QuestionTargetDao;
import com.sunchs.lyt.question.dao.ipml.QuestionTargetDaoImpl;
import com.sunchs.lyt.question.enums.QuestionnaireStatusEnum;
import com.sunchs.lyt.question.util.SpringContext;

import java.util.List;

public class QuestionnaireData extends Questionnaire {

    private String statusName;

    private String targetOneName;

    private String updateTimeName;

    private String hospitalName;

    private List<QuestionData> questionList;

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getTargetOneName() {
        return targetOneName;
    }

    public void setTargetOneName(String targetOneName) {
        this.targetOneName = targetOneName;
    }

    public String getUpdateTimeName() {
        return updateTimeName;
    }

    public void setUpdateTimeName(String updateTimeName) {
        this.updateTimeName = updateTimeName;
    }

    public List<QuestionData> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<QuestionData> questionList) {
        this.questionList = questionList;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public void initData() {
        // 状态名称
        setStatusName(QuestionnaireStatusEnum.get(getStatus()));
        // 更新时间
        setUpdateTimeName(FormatUtil.dateTime(getUpdateTime()));
        // 一级指标
        QuestionTargetDaoImpl targetDao = SpringContext.getBean(QuestionTargetDaoImpl.class);
        setTargetOneName(targetDao.getNameById(getTargetOne()));
    }
}
