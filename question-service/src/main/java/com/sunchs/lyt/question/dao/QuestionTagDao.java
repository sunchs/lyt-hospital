package com.sunchs.lyt.question.dao;

import com.sunchs.lyt.question.bean.QuestionTagData;
import com.sunchs.lyt.question.bean.TagData;

import java.util.List;
import java.util.Map;

public interface QuestionTagDao {

    /**
     * 根据 指标ID 获取指标信息
     */
    QuestionTagData getById(int id);

    /**
     * 根据 指标ID 获取指标列表
     */
    List<QuestionTagData> getList(int id);

    /**
     * 根据 指标ID 获取指标总条数
     */
    int getCount(int id);

    /**
     * 添加指标
     */
    int insert(Map<String, Object> param);

    /**
     * 修改指标
     */
    int update(Map<String, Object> param);

    /**
     * 根据 问题ID 获取问题标签
     */
    List<TagData> getQuestionTag(int questionId);

    /**
     * 根据 标签ID 获取标签名称
     */
    String getNameById(int id);
}
