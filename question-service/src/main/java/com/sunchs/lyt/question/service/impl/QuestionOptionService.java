package com.sunchs.lyt.question.service.impl;

import com.sunchs.lyt.db.business.entity.OptionTemplate;
import com.sunchs.lyt.question.bean.OptionBean;
import com.sunchs.lyt.question.bean.QuestionOptionData;
import com.sunchs.lyt.question.bean.QuestionOptionParam;
import com.sunchs.lyt.question.dao.ipml.QuestionOptionDaoImpl;
import com.sunchs.lyt.question.service.IQuestionOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionOptionService implements IQuestionOptionService {

    @Autowired
    QuestionOptionDaoImpl questionOptionDao;

    @Override
    public void save(QuestionOptionParam param) {
        OptionTemplate optionTemplate = new OptionTemplate();
        optionTemplate.setId(param.getId());
        optionTemplate.setRemarks(param.getRemarks());
        if (questionOptionDao.update(optionTemplate)) {
            questionOptionDao.deleteOption(param.getId());
            List<OptionBean> optionList = param.getOptionList();
            for (OptionBean option : optionList) {
                questionOptionDao.insertOption(param.getId(), option.getOptionContent());
            }
        }
    }

    @Override
    public QuestionOptionData getInfo(QuestionOptionParam param) {
        QuestionOptionData info = questionOptionDao.getInfo(param.getId());
        if (info != null) {
            info.setOptionList(questionOptionDao.getOptionList(param.getId()));
        }
        return info;
    }

    @Override
    public List<QuestionOptionData> getList(QuestionOptionParam param) {
        List<QuestionOptionData> typeList = questionOptionDao.getTypeList();
        for (QuestionOptionData type : typeList) {
            type.setOptionList(questionOptionDao.getOptionList(type.getId()));
        }
        return typeList;
    }
}
