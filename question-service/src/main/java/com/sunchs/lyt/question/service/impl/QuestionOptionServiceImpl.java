package com.sunchs.lyt.question.service.impl;

import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.question.bean.QuestionOptionData;
import com.sunchs.lyt.question.bean.QuestionOptionParam;
import com.sunchs.lyt.question.bean.QuestionTargetParam;
import com.sunchs.lyt.question.dao.QuestionOptionDao;
import com.sunchs.lyt.question.dao.ipml.QuestionOptionDaoImpl;
import com.sunchs.lyt.question.service.QuestionOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionOptionServiceImpl implements QuestionOptionService {

    @Autowired
    QuestionOptionDaoImpl questionOptionDao;

    @Override
    public QuestionOptionData save(QuestionOptionParam param) {
        Map<String, Object> opt = new HashMap<>();
        opt.put("id", param.getId());
        opt.put("remarks", param.getRemarks());
        int id = questionOptionDao.update(opt);
        if (id > 0) {
            questionOptionDao.deleteOption(id);
            List<String> optionList = param.getOptionList();
            for (String option : optionList) {
                questionOptionDao.insertOption(id, option);
            }
        }
        return new QuestionOptionData();
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
