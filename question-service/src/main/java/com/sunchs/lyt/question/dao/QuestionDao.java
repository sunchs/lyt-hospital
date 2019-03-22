package com.sunchs.lyt.question.dao;

import com.sunchs.lyt.question.bean.QuestionData;

import java.util.Map;

public interface QuestionDao {

    /**
     * 根据 问题ID 获取问题信息
     */
    QuestionData getById(Integer id);

    /**
     * 添加问题
     */
    Integer insert(Map<String, Object> param);

    /**
     * 修改问题
     */
    Integer update(Map<String, Object> param);

    /**
     * 根据 问题ID 添加选项
     */
    void insertQuestionOption(Map<String, Object> param);

    /**
     * 根据 问题ID 删除选项
     */
    void deleteQuestionOption(Integer questionId);
}
