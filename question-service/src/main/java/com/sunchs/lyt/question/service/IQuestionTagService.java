package com.sunchs.lyt.question.service;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.question.bean.QuestionTagData;
import com.sunchs.lyt.question.bean.QuestionTagParam;

import java.util.List;
import java.util.Map;

public interface IQuestionTagService {

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

    /**
     * 获取 级联 数据
     */
    List<Map<String, Object>> getCascaderData();
}
