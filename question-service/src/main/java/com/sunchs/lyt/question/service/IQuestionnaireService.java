package com.sunchs.lyt.question.service;

import com.sunchs.lyt.db.business.entity.Questionnaire;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.question.bean.QuestionnaireData;
import com.sunchs.lyt.question.bean.QuestionnaireParam;

public interface IQuestionnaireService {

    /**
     * 根据 问卷ID 获取问卷详情
     */
    QuestionnaireData getById(int id);

    /**
     * 问卷分页
     */
    PagingList<Questionnaire> getPageList(QuestionnaireParam param);

    /**
     * 保存数据
     */
    void save(QuestionnaireParam param);

    /**
     * 根据 问卷ID 生产xls文件
     */
    String createExcelFile(int id);
}
