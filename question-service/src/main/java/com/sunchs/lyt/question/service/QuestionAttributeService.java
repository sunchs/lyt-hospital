package com.sunchs.lyt.question.service;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.question.bean.QuestionAttributeData;
import com.sunchs.lyt.question.bean.QuestionAttributeParam;

public interface QuestionAttributeService {

    /**
     * 根据 属性ID 获取属性信息
     */
    QuestionAttributeData getById(Integer id);

    /**
     * 根据 属性ID 获取属性列表
     */
    PagingList<QuestionAttributeData> getList(Integer id);

    /**
     * 保存数据
     */
    QuestionAttributeData save(QuestionAttributeParam param);
}
