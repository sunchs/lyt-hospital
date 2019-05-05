package com.sunchs.lyt.question.dao;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.Question;
import com.sunchs.lyt.question.bean.QuestionData;

public interface QuestionDao {

    /**
     * 根据 问题ID 获取问题信息
     */
    QuestionData getById(int questionId);

    /**
     * 获取问题分页信息
     */
    Page<Question> getPaging(Wrapper<Question> where, int pageNow, int pageSize);

    /**
     * 添加问题
     */
    boolean insert(Question question);

    /**
     * 修改问题
     */
    boolean update(Question question);
}
