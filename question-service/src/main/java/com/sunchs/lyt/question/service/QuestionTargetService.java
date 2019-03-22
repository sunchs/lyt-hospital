package com.sunchs.lyt.question.service;

import com.sunchs.lyt.question.bean.QuestionTargetParam;
import com.sunchs.lyt.question.bean.QuestionTargetData;

public interface QuestionTargetService {

    /**
     * 根据 指标ID 获取指标信息
     */
    QuestionTargetData getById(Integer id);

    /**
     * 保存数据
     */
    QuestionTargetData save(QuestionTargetParam param);

}
