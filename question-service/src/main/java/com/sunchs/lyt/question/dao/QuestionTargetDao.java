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
     * 获取指标所有数据
     */
    List<QuestionTargetData> getAll();

    /**
     * 根据 指标ID 获取指标名称
     */
    String getNameById(Integer id);

    /**
     * 根据 指标ID 获取指标总条数
     */
    int getCount(Integer id);

    /**
     * 添加指标
     */
    int insert(Map<String, Object> param);

    /**
     * 修改指标
     */
    int update(Map<String, Object> param);
}
