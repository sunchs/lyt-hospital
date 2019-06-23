package com.sunchs.lyt.question.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.OptionTemplate;
import com.sunchs.lyt.db.business.entity.Question;
import com.sunchs.lyt.db.business.entity.QuestionOption;
import com.sunchs.lyt.db.business.entity.QuestionTagBinding;
import com.sunchs.lyt.db.business.service.impl.QuestionTagBindingServiceImpl;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.framework.util.PagingUtil;
import com.sunchs.lyt.question.bean.QuestionData;
import com.sunchs.lyt.question.bean.QuestionParam;
import com.sunchs.lyt.question.bean.TagParam;
import com.sunchs.lyt.question.dao.QuestionDao;
import com.sunchs.lyt.question.dao.ipml.QuestionOptionDaoImpl;
import com.sunchs.lyt.question.exception.QuestionException;
import com.sunchs.lyt.question.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class QuestionService implements IQuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private QuestionOptionDaoImpl questionOptionDao;

    @Autowired
    private QuestionTagBindingServiceImpl questionTagBindingService;

    @Override
    public void save(QuestionParam param) {
        if (NumberUtil.isZero(param.getId())) {
            insert(param);
        } else {
            update(param);
        }
    }

    @Override
    public PagingList<QuestionData> getPageList(QuestionParam param) {
        Wrapper<Question> where = new EntityWrapper<>();
        if (param.getTargetOne() > 0) {
            where.eq(Question.TARGET_ONE, param.getTargetOne());
        }
        Page<Question> data = questionDao.getPaging(where, param.getPageNow(), param.getPageSize());
        List<QuestionData> list = new ArrayList<>();
        for (Question question : data.getRecords()) {
            list.add(questionDao.getById(question.getId()));
        }
        return PagingUtil.getData(list, data.getTotal(), data.getCurrent(), data.getSize());
    }

    private void insert(QuestionParam param) {
        // 获取选项数据
        OptionTemplate optionTemplate = questionOptionDao.getOptionById(param.getOptionId());
        if (Objects.isNull(optionTemplate)) {
            throw new QuestionException("选项模版被删除，请重新选择");
        }

        Question question = new Question();
        // TODO::医院ID
        question.setHospitalId(0);
        question.setNumber(param.getNumber());
        question.setTitle(param.getTitle());
        question.setStatus(1);
        question.setTargetOne(param.getTargetOne());
        question.setTargetTwo(param.getTargetTwo());
        question.setTargetThree(param.getTargetThree());
        question.setOptionType(optionTemplate.getPid());
        question.setRemark(param.getRemark());
        // TODO::用户ID
        question.setUpdateId(0);
        question.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        // TODO::用户ID
        question.setCreateId(0);
        question.setCreateTime(new Timestamp(System.currentTimeMillis()));
        if (questionDao.insert(question)) {
            // 插入选项
            setQuestionOption(question.getId(), optionTemplate);
            // 插入标签
            setQuestionAttribute(question.getId(), param.getTagList());
        }
    }

    private void update(QuestionParam param) {
        Question question = new Question();
        question.setStatus(param.getStatus());
        question.setRemark(param.getRemark());
        // TODO::用户ID
        question.setUpdateId(0);
        question.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        questionDao.update(question);
    }

    private void setQuestionOption(int questionId, OptionTemplate optionTemplate) {
        String content = optionTemplate.getContent();
        String[] split = content.split(",");
        int index = 1;
        for (String value : split) {
            QuestionOption data = new QuestionOption();
            data.setQuestionId(questionId);
            data.setTitle(value);
            data.setSort(index);
            questionOptionDao.insertQuestionOption(data);
            index++;
        }
    }

    private void setQuestionAttribute(int questionId, List<TagParam> tagParam) {
        if (tagParam == null || tagParam.size() == 0) {
            return;
        }
        for (TagParam param : tagParam) {
            if (Objects.nonNull(param) && NumberUtil.nonZero(param.getTagId())) {
                QuestionTagBinding data = new QuestionTagBinding();
                data.setQuestionId(questionId);
                data.setTagType(param.getType());
                data.setTagId(param.getTagId());
                questionTagBindingService.insert(data);
            }
        }
    }
}