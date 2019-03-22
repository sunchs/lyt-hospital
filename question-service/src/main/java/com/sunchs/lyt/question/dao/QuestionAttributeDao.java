package com.sunchs.lyt.question.dao;

import com.sunchs.lyt.question.bean.QuestionAttributeData;

import java.util.List;
import java.util.Map;

public interface QuestionAttributeDao {

    /**
     * 根据 指标ID 获取指标信息
     */
    QuestionAttributeData getById(Integer id);

    /**
     * 根据 指标ID 获取指标列表
     */
    List<QuestionAttributeData> getList(Integer id);

    /**
     * 根据 指标ID 获取指标总条数
     */
    Integer getCount(Integer id);

    /**
     * 添加指标
     */
    Integer insert(Map<String, Object> param);

    /**
     * 修改指标
     */
    Integer update(Map<String, Object> param);
}
