package com.sunchs.lyt.question.service.impl;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.framework.util.PagingUtil;
import com.sunchs.lyt.question.bean.*;
import com.sunchs.lyt.question.dao.QuestionnaireDao;
import com.sunchs.lyt.question.service.IQuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionnaireService implements IQuestionnaireService {

    @Autowired
    private QuestionnaireDao questionnaireDao;

    @Override
    public PagingList<QuestionnaireData> getPageList(QuestionnaireParam param) {
        int total = questionnaireDao.getCount(param);
        List<QuestionnaireData> pageList = questionnaireDao.getPageList(param);
        return PagingUtil.getData(pageList, total, param.getPageNow(), param.getPageSize());
    }

    @Override
    public QuestionnaireData save(QuestionnaireParam param) {
        Integer id = 0;
        if (NumberUtil.isZero(param.getId())) {
            id = insert(param);
        } else {
            id = update(param);
        }
        if (id > 0) {

        }
        return null;
    }

    private int insert(QuestionnaireParam param) {
        // 参数检查

        Map<String, Object> opt = new HashMap<>();
        opt.put("title", param.getTitle());
        opt.put("updateId", 0);
        opt.put("updateTime", new Timestamp(System.currentTimeMillis()));
        opt.put("createId", 0);
        opt.put("createTime", new Timestamp(System.currentTimeMillis()));
        int id = questionnaireDao.insert(opt);
        if (id > 0) {

        }
        return id;
    }

    private int update(QuestionnaireParam param) {
        Map<String, Object> opt = new HashMap<>();
        opt.put("id", param.getId());
        if (param.getStatus() != null) {
            opt.put("status", param.getStatus());
            opt.put("updateId", 0);
            opt.put("updateTime", new Timestamp(System.currentTimeMillis()));
        }
        return questionnaireDao.update(opt);
    }
}