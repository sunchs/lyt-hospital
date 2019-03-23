package com.sunchs.lyt.question.dao;

import com.sunchs.lyt.question.bean.OptionData;

import java.util.List;

public interface QuestionOptionDao {

    /**
     * 根据 问题ID 获取选项列表
     * @param questionId
     */
    List<OptionData> getListById(Integer questionId);

}
