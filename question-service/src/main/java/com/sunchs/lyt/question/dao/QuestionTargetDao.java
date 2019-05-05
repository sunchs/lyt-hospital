package com.sunchs.lyt.question.dao;

import com.sunchs.lyt.db.business.entity.QuestionTarget;
import com.sunchs.lyt.question.bean.QuestionTargetData;

import java.util.List;

public interface QuestionTargetDao {

    /**
     * 根据 指标ID 获取指标信息
     */
    QuestionTargetData getById(int id);
//
//    /**
//     * 根据 指标ID 获取指标列表
//     */
//    List<QuestionTargetData> getList(int id);

    /**
     * 获取指标所有数据
     */
    List<QuestionTarget> getAll();

    /**
     * 根据 指标ID 获取指标名称
     */
    String getNameById(int id);

//    /**
//     * 根据 指标ID 获取指标总条数
//     */
//    int getCount(int id);

    /**
     * 添加指标
     */
    boolean insert(QuestionTarget questionTarget);

//    /**
//     * 修改指标
//     */
//    int update(Map<String, Object> param);

    /**
     * 标题数量
     */
    int titleQty(String title, int target);
}
