package com.sunchs.lyt.question.service.impl;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.framework.util.PagingUtil;
import com.sunchs.lyt.question.bean.*;
import com.sunchs.lyt.question.dao.QuestionDao;
import com.sunchs.lyt.question.dao.ipml.QuestionOptionDaoImpl;
import com.sunchs.lyt.question.exception.QuestionException;
import com.sunchs.lyt.question.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionService implements IQuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private QuestionOptionDaoImpl questionOptionDao;

    @Override
    public QuestionData save(QuestionParam param) {
        int questionId = 0;
        if (param.getId() > 0) {
            questionId = insert(param);
        } else {
            questionId = update(param);
        }
        if (questionId > 0) {
            QuestionData question = questionDao.getById(questionId);
            if (question == null) {
                throw new QuestionException("问题ID：" + questionId + "，不存在");
            } else {
//                user.setRoleList(roleDao.getRoleByUserId(userId));
            }
            return question;
        }
        return null;
    }

    @Override
    public PagingList<QuestionData> getPageList(QuestionParam param) {
        int total = questionDao.getCount(param);
        List<QuestionData> pageList = questionDao.getPageList(param);
        return PagingUtil.getData(pageList, total, param.getPageNow(), param.getPageSize());
    }

    private int insert(QuestionParam param) {
        Map<String, Object> opt = new HashMap<>();
        opt.put("title", param.getTitle());
        opt.put("remark", param.getRemark());
        opt.put("updateId", 0);
        opt.put("updateTime", new Timestamp(System.currentTimeMillis()));
        opt.put("createId", 0);
        opt.put("createTime", new Timestamp(System.currentTimeMillis()));
        if (param.getTarget() != null) {
            opt.put("targetOne", param.getTarget().getOne());
            opt.put("targetTwo", param.getTarget().getTwo());
            opt.put("targetThree", param.getTarget().getThree());
        }
        if (param.getOption() != null) {
            opt.put("optionType", param.getOption().getType());
        }
        int questionId = questionDao.insert(opt);
        if (questionId > 0) {
            // 插入选项
            resetQuestionOption(questionId, param.getOption().getOptionId());
            // 插入标签
            resetQuestionAttribute(questionId, param.getTagList());
        }
        return questionId;
    }

    private int update(QuestionParam param) {
        Map<String, Object> opt = new HashMap<>();
        opt.put("id", param.getId());
        if (param.getStatus() != null) {
            opt.put("status", param.getStatus());
            opt.put("updateId", 0);
            opt.put("updateTime", new Timestamp(System.currentTimeMillis()));
        }
        int questionId = questionDao.update(opt);
        if (questionId > 0) {
//            userDao.saveUserRole(questionId, param.getRole());
        }
        return questionId;
    }

    private void resetQuestionOption(int questionId, int optionId) {
        questionDao.deleteQuestionOption(questionId);
        OptionBean option = questionOptionDao.getOption(optionId);
        if (option != null) {
            String optionContent = option.getOptionContent();
            String[] split = optionContent.split(",");
            int index = 10;
            for (String optionValue : split) {
                Map<String, Object> opt = new HashMap<>();
                opt.put("questionId", questionId);
                opt.put("title", optionValue);
                opt.put("sort", index);
                questionDao.insertQuestionOption(opt);
                index++;
            }
        }

//        for (OptionParam param : paramList) {
//            Map<String, Object> opt = new HashMap<>();
//            opt.put("questionId", questionId);
//            opt.put("title", param.getOptionName());
//            opt.put("sort", param.getSort());
//            questionDao.insertQuestionOption(opt);
//        }
    }

    private void resetQuestionAttribute(int questionId, List<TagParam> attribute) {
        questionDao.deleteQuestionAttribute(questionId);
        if (attribute != null && attribute.size() > 0) {
            attribute.forEach(param -> {
                Map<String, Object> opt = new HashMap<>();
                opt.put("questionId", questionId);
                opt.put("tagType", param.getType());
                opt.put("tagId", param.getTagId());
                questionDao.insertQuestionAttribute(opt);
            });
        }
    }
}