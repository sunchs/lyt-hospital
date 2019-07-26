package com.sunchs.lyt.question.service;

import com.sunchs.lyt.db.business.entity.Questionnaire;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.question.bean.QuestionnaireData;
import com.sunchs.lyt.question.bean.QuestionnaireParam;

import java.util.List;
import java.util.Map;

public interface IQuestionnaireService {

    /**
     * 根据 问卷ID 获取问卷详情
     */
    QuestionnaireData getById(int id);

    /**
     * 问卷分页
     */
    PagingList<QuestionnaireData> getPageList(QuestionnaireParam param);

    /**
     * 保存数据
     */
    int save(QuestionnaireParam param);

    /**
     * 更新状态
     */
    void updateStatus(QuestionnaireParam param);

    /**
     * 获取可用问卷
     */
    List<Map<String, Object>> getUsableList(QuestionnaireParam param);

    /**
     * 根据 问卷ID 生产xls文件
     */
    String createExcelFile(int id);
}
