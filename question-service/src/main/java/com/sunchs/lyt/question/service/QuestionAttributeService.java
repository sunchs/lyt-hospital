package com.sunchs.lyt.question.service;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.question.bean.QuestionTagData;
import com.sunchs.lyt.question.bean.QuestionTagParam;

public interface QuestionAttributeService {

    /**
     * 根据 属性ID 获取属性信息
     */
    QuestionTagData getById(Integer id);

    /**
     * 根据 属性ID 获取属性列表
     */
    PagingList<QuestionTagData> getList(Integer id);

    /**
     * 保存数据
     */
    QuestionTagData save(QuestionTagParam param);
}
