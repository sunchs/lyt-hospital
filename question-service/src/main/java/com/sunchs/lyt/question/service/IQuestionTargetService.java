package com.sunchs.lyt.question.service;

import com.sunchs.lyt.question.bean.QuestionTargetData;
import com.sunchs.lyt.question.bean.QuestionTargetParam;

import java.util.List;
import java.util.Map;

public interface IQuestionTargetService {

    /**
     * 根据 指标ID 获取指标信息
     */
    QuestionTargetData getById(int id);

//    /**
//     * 根据 指标ID 获取指标列表
//     */
//    PagingList<QuestionTargetData> getList(int id);

    /**
     * 获取所有指标数据
     */
    List<QuestionTargetData> getAll();

    /**
     * 更新状态
     */
    void updateStatus(QuestionTargetParam param);

    /**
     * 保存数据
     */
    int save(QuestionTargetParam param);

    /**
     * 获取 级联 数据
     */
    List<Map<String, Object>> getCascaderData();
}
