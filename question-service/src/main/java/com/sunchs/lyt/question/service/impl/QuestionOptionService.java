package com.sunchs.lyt.question.service.impl;

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
    public QuestionOptionData save(QuestionOptionParam param) {
        Map<String, Object> opt = new HashMap<>();
        opt.put("id", param.getId());
        opt.put("remarks", param.getRemarks());
        int id = questionOptionDao.update(opt);
        if (id > 0) {
            questionOptionDao.deleteOption(id);
            List<OptionBean> optionList = param.getOptionList();
            for (OptionBean option : optionList) {
                questionOptionDao.insertOption(id, option.getOptionContent());
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
