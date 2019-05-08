package com.sunchs.lyt.question.dao;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.Questionnaire;
import com.sunchs.lyt.db.business.entity.QuestionnaireExtend;

import java.util.List;

public interface QuestionnaireDao {

    /**
     * 根据 问卷ID 获取问卷基本信息
     */
    Questionnaire getById(int id);

    /**
     * 获取 问卷分页 数据
     */
    Page<Questionnaire> getPaging(Wrapper<Questionnaire> where, int pageNow, int pageSize);

    /**
     * 根据 问卷ID 获取问卷题目
     */
    List<QuestionnaireExtend> getExtendById(int id);

    /**
     * 添加问卷
     */
    boolean insert(Questionnaire entity);

    /**
     *
     */
    boolean insertQuestion(QuestionnaireExtend entity);

    /**
     * 修改问卷
     */
    boolean update(Questionnaire entity);
}
