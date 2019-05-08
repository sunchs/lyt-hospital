package com.sunchs.lyt.question.dao.ipml;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.Questionnaire;
import com.sunchs.lyt.db.business.entity.QuestionnaireExtend;
import com.sunchs.lyt.db.business.service.impl.QuestionnaireExtendServiceImpl;
import com.sunchs.lyt.db.business.service.impl.QuestionnaireServiceImpl;
import com.sunchs.lyt.question.dao.QuestionnaireDao;
import com.sunchs.lyt.question.exception.QuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuestionnaireDaoImpl implements QuestionnaireDao {

    @Autowired
    private QuestionnaireServiceImpl questionnaireService;

    @Autowired
    private QuestionnaireExtendServiceImpl questionnaireExtendService;

    @Override
    public Questionnaire getById(int id) {
        Wrapper<Questionnaire> wrapper = new EntityWrapper<>();
        wrapper.eq("id", id);
        return questionnaireService.selectOne(wrapper);
    }

    @Override
    public Page<Questionnaire> getPaging(Wrapper<Questionnaire> where, int pageNow, int pageSize) {
        try {
            Page<Questionnaire> limit = new Page<>(pageNow, pageSize);
            return questionnaireService.selectPage(limit, where);
        } catch (Exception e) {
            throw new QuestionException("查询问卷数据 --> 异常:" + e.getMessage());
        }
    }

    @Override
    public List<QuestionnaireExtend> getExtendById(int id) {
        Wrapper<QuestionnaireExtend> wrapper = new EntityWrapper<>();
        wrapper.eq("questionnaire_id", id);
        return questionnaireExtendService.selectList(wrapper);
    }

    @Override
    public boolean insert(Questionnaire entity) {
        try {
            return questionnaireService.insert(entity);
        } catch (Exception e) {
            throw new QuestionException("添加问卷数据 --> 异常:" + e.getMessage());
        }
    }

    @Override
    public boolean update(Questionnaire entity) {
        try {
            return questionnaireService.updateById(entity);
        } catch (Exception e) {
            throw new QuestionException("修改问卷状态 --> 异常:" + e.getMessage());
        }
    }

    @Override
    public boolean insertQuestion(QuestionnaireExtend entity) {
        try {
            return questionnaireExtendService.insert(entity);
        } catch (Exception e) {
            throw new QuestionException("添加问卷题目数据 --> 异常:" + e.getMessage());
        }
    }
}
