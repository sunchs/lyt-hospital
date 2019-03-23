package com.sunchs.lyt.question.dao;

import com.sunchs.lyt.question.bean.QuestionData;
import com.sunchs.lyt.question.bean.QuestionParam;

import java.util.List;
import java.util.Map;

public interface QuestionDao {

    /**
     * 根据 问题ID 获取问题信息
     */
    QuestionData getById(Integer id);

    /**
     * 获取 问题分页 数据
     */
    List<QuestionData> getPageList(QuestionParam param);

    /**
     * 获取 问题 总条数
     */
    int getCount(QuestionParam param);

    /**
     * 添加问题
     */
    Integer insert(Map<String, Object> param);

    /**
     * 修改问题
     */
    int update(Map<String, Object> param);

    /**
     * 根据 问题ID 添加选项
     */
    void insertQuestionOption(Map<String, Object> param);

    /**
     * 根据 问题ID 删除选项
     */
    void deleteQuestionOption(Integer questionId);
}
