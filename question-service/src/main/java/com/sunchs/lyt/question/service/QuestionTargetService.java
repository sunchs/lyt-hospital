package com.sunchs.lyt.question.service;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.question.bean.QuestionTargetData;
import com.sunchs.lyt.question.bean.QuestionTargetParam;

public interface QuestionTargetService {

    /**
     * 根据 指标ID 获取指标信息
     */
    QuestionTargetData getById(Integer id);

    /**
     * 根据 指标ID 获取指标列表
     */
    PagingList<QuestionTargetData> getList(Integer id);

    /**
     * 保存数据
     */
    QuestionTargetData save(QuestionTargetParam param);

}
