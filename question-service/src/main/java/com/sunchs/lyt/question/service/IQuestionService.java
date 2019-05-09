package com.sunchs.lyt.question.service;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.question.bean.QuestionData;
import com.sunchs.lyt.question.bean.QuestionParam;

public interface IQuestionService {

    /**
     * 保存数据
     */
    void save(QuestionParam param);

    /**
     * 问题分页列表
     */
    PagingList<QuestionData> getPageList(QuestionParam param);
}
