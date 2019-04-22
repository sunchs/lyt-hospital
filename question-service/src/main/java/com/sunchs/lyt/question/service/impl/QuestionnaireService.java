package com.sunchs.lyt.question.service.impl;

import com.sunchs.lyt.db.business.entity.Questionnaire;
import com.sunchs.lyt.db.business.entity.QuestionnaireExtend;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.util.JsonUtil;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.framework.util.PagingUtil;
import com.sunchs.lyt.question.bean.*;
import com.sunchs.lyt.question.dao.QuestionnaireDao;
import com.sunchs.lyt.question.dao.ipml.QuestionOptionDaoImpl;
import com.sunchs.lyt.question.service.IQuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class QuestionnaireService implements IQuestionnaireService {

    @Autowired
    private QuestionOptionDaoImpl questionOptionDao;

    @Autowired
    private QuestionnaireDao questionnaireDao;

    @Override
    public PagingList<QuestionnaireData> getPageList(QuestionnaireParam param) {
        int total = questionnaireDao.getCount(param);
        List<QuestionnaireData> pageList = questionnaireDao.getPageList(param);
        return PagingUtil.getData(pageList, total, param.getPageNow(), param.getPageSize());
    }

    @Override
    public void save(QuestionnaireParam param) {
        if (NumberUtil.isZero(param.getId())) {
            insert(param);
        } else {
            update(param);
        }
    }

    private void insert(QuestionnaireParam param) {
        Questionnaire data = new Questionnaire();
        data.setTitle(param.getTitle());
        data.setStatus(param.getStatus());
        data.setTargetOne(param.getTargetOne());
        // TODO::用户ID
        data.setUpdateId(0);
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        // TODO::用户ID
        data.setCreateId(0);
        data.setCreateTime(new Timestamp(System.currentTimeMillis()));
        if (questionnaireDao.insert(data)) {
            Integer questionnaireId = data.getId();
            List<QuestionBean> questionList = param.getQuestion();
            for (QuestionBean question : questionList) {
                QuestionnaireExtend extend = new QuestionnaireExtend();
                extend.setQuestionnaireId(questionnaireId);
                extend.setQuestionId(question.getQuestionId());
                extend.setSkipMode(question.getSkipMode());
                if (question.getSkipMode() == 1) {
                    extend.setSkipQuestionId(question.getSkipQuestionId());
                } else if (question.getSkipMode() == 2) {
                    String skipBody = setSkipContent(question.getQuestionId(), question.getSkipContent());
                    extend.setSkipContent(skipBody);
                }
                questionnaireDao.insertQuestion(extend);
            }
        }
    }

    /**
     * 构建 选项跳转 的字符串
     */
    private String setSkipContent(int questionId, List<OptionSkipParam> skipList) {
        List<OptionSkipParam> skipContent = new ArrayList<>();
        List<OptionData> optionList = questionOptionDao.getListById(questionId);
        if (optionList.size() > 0) {
            for (OptionData option : optionList) {
                OptionSkipParam opt = new OptionSkipParam();
                opt.setOptionId(option.getOptionId());
                opt.setSkipQuestionId(setSkipQuestionId(option, skipList));
                skipContent.add(opt);
            }
        }
        return JsonUtil.toJson(skipContent);
    }

    /**
     * 提取有效跳转题目ID
     */
    private int setSkipQuestionId(OptionData option, List<OptionSkipParam> skipList) {
        if (Objects.nonNull(skipList) && skipList.size() > 0) {
            for (OptionSkipParam skip : skipList) {
                if (skip.getOptionId() == option.getOptionId()) {
                    return skip.getSkipQuestionId();
                }
            }
        }
        return 0;
    }

    private void update(QuestionnaireParam param) {
        Questionnaire data = new Questionnaire();
        data.setId(param.getId());
        data.setStatus(param.getStatus());
        // TODO::用户ID
        data.setUpdateId(0);
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        questionnaireDao.update(data);
    }
}