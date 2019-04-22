package com.sunchs.lyt.question.service;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.question.bean.QuestionnaireData;
import com.sunchs.lyt.question.bean.QuestionnaireParam;

public interface IQuestionnaireService {

    /**
     * 问卷分页
     */
    PagingList<QuestionnaireData> getPageList(QuestionnaireParam param);

    /**
     * 保存数据
     */
    QuestionnaireData save(QuestionnaireParam param);
}
