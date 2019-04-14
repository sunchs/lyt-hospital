package com.sunchs.lyt.question.dao;

import com.sunchs.lyt.question.bean.QuestionBean;
import com.sunchs.lyt.question.bean.QuestionnaireData;
import com.sunchs.lyt.question.bean.QuestionnaireParam;

import java.util.List;
import java.util.Map;

public interface QuestionnaireDao {

    /**
     * 获取 问卷 总条数
     */
    int getCount(QuestionnaireParam param);

    /**
     * 获取 问卷分页 数据
     */
    List<QuestionnaireData> getPageList(QuestionnaireParam param);

    /**
     * 添加问卷
     */
    int insert(Map<String, Object> param);

    /**
     *
     */
    int insertQuestion(int wjId, QuestionBean questionBean);

    /**
     * 修改问卷
     */
    int update(Map<String, Object> param);
}
