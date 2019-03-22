package com.sunchs.lyt.question.dao;

import com.sunchs.lyt.question.bean.QuestionTargetData;

import java.util.List;
import java.util.Map;

public interface QuestionTargetDao {

    /**
     * 根据 指标ID 获取指标信息
     */
    QuestionTargetData getById(Integer id);

    /**
     * 根据 指标ID 获取指标列表
     */
    List<QuestionTargetData> getList(Integer id);

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
