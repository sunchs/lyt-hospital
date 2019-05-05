package com.sunchs.lyt.question.service;

import com.sunchs.lyt.db.business.entity.Questionnaire;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.question.bean.QuestionnaireParam;

public interface IQuestionnaireService {

    /**
     * 问卷分页
     */
    PagingList<Questionnaire> getPageList(QuestionnaireParam param);

    /**
     * 保存数据
     */
    void save(QuestionnaireParam param);
}
