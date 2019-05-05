package com.sunchs.lyt.question.dao;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.Questionnaire;
import com.sunchs.lyt.db.business.entity.QuestionnaireExtend;
import com.sunchs.lyt.question.bean.QuestionBean;
import com.sunchs.lyt.question.bean.QuestionnaireData;
import com.sunchs.lyt.question.bean.QuestionnaireParam;

import java.util.List;
import java.util.Map;

public interface QuestionnaireDao {

    /**
     * 获取 问卷分页 数据
     */
    Page<Questionnaire> getPaging(Wrapper<Questionnaire> where, int pageNow, int pageSize);

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
