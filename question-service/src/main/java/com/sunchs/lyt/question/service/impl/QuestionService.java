package com.sunchs.lyt.question.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.*;
import com.sunchs.lyt.db.business.service.impl.QuestionServiceImpl;
import com.sunchs.lyt.db.business.service.impl.QuestionTagBindingServiceImpl;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.enums.UserTypeEnum;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.framework.util.PagingUtil;
import com.sunchs.lyt.framework.util.StringUtil;
import com.sunchs.lyt.framework.util.UserThreadUtil;
import com.sunchs.lyt.question.bean.QuestionData;
import com.sunchs.lyt.question.bean.QuestionParam;
import com.sunchs.lyt.question.bean.TagParam;
import com.sunchs.lyt.question.dao.QuestionDao;
import com.sunchs.lyt.question.dao.ipml.QuestionOptionDaoImpl;
import com.sunchs.lyt.question.enums.QuestionStatusEnum;
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

    @Autowired
    private QuestionServiceImpl questionService;

    @Override
    public void save(QuestionParam param) {
        if (NumberUtil.isZero(param.getId())) {
            insert(param);
        } else {
            update(param);
        }
    }

    @Override
    public QuestionData getById(int questionId) {
        return questionDao.getById(questionId);
    }

    @Override
    public void updateInfo(QuestionParam param) {
        if (param.getId() == 0) {
            throw new QuestionException("题目ID不能为空");
        }

        Question data = new Question();
        data.setId(param.getId());
        data.setTargetOne(param.getTargetOne());
        data.setTargetTwo(param.getTargetTwo());
        data.setTargetThree(param.getTargetThree());
        data.setRemark(param.getRemark());
        data.setUpdateId(UserThreadUtil.getUserId());
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        if (questionService.updateById(data)) {
            // 插入标签
            setQuestionAttribute(data.getId(), param.getTagList());
        }
    }

    @Override
    public PagingList<QuestionData> getPageList(QuestionParam param) {
        Wrapper<Question> where = new EntityWrapper<>();
        if (param.getTargetOne() > 0) {
            where.eq(Question.TARGET_ONE, param.getTargetOne());
            where.eq(Question.STATUS, QuestionStatusEnum.Enabled.status);
        }
        // 关键词搜索
        if (StringUtil.isNotEmpty(param.getKeyword())) {
            where.like(Question.TITLE, param.getKeyword());
        }

        if (UserThreadUtil.getType() == UserTypeEnum.ADMIN.value) {
            if (NumberUtil.nonZero(param.getHospitalId())) {
                where.andNew(Question.HOSPITAL_ID + "={0} OR "+Question.IS_PUBLIC+"=1", param.getHospitalId());
            }
        } else if (UserThreadUtil.getHospitalId() > 0){
            where.andNew(Question.HOSPITAL_ID + "={0} OR "+Question.IS_PUBLIC+"=1", UserThreadUtil.getHospitalId());
        } else {
            return PagingUtil.Empty(param.getPageNow(), param.getPageSize());
        }
        where.orderBy(Question.ID, false);

        Page<Question> data = questionService.selectPage(new Page<>(param.getPageNow(), param.getPageSize()), where);
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

        Wrapper<Question> pWrapper = new EntityWrapper<Question>()
                .eq(Question.IS_PUBLIC, 1)
                .eq(Question.TITLE, param.getTitle())
                .eq(Question.STATUS, 1);
        int count0 = questionService.selectCount(pWrapper);
        if (count0 > 0) {
            throw new QuestionException("题目已存在，请重新输入！");
        }

        Wrapper<Question> questionWrapper = new EntityWrapper<Question>()
                .eq(Question.TARGET_ONE, param.getTargetOne())
                .eq(Question.TITLE, param.getTitle())
                .eq(Question.STATUS, 1)
                .eq(Question.HOSPITAL_ID, UserThreadUtil.getHospitalId());
        int count = questionService.selectCount(questionWrapper);
        if (count > 0) {
            throw new QuestionException("题目已存在，请重新输入！");
        }

        Wrapper<Question> noWrapper = new EntityWrapper<Question>()
                .eq(Question.IS_PUBLIC, 1)
                .eq(Question.NUMBER, param.getNumber())
                .eq(Question.STATUS, 1);
        int noCout = questionService.selectCount(noWrapper);
        if (noCout > 0) {
            throw new QuestionException("题目编号已存在，请重新输入！");
        }

        Wrapper<Question> noQuestionWrapper = new EntityWrapper<Question>()
                .eq(Question.TARGET_ONE, param.getTargetOne())
                .eq(Question.NUMBER, param.getNumber())
                .eq(Question.STATUS, 1)
                .eq(Question.HOSPITAL_ID, UserThreadUtil.getHospitalId());
        int noQuestionCount = questionService.selectCount(noQuestionWrapper);
        if (noQuestionCount > 0) {
            throw new QuestionException("题目编号已存在，请重新输入！");
        }

        Question question = new Question();
        question.setHospitalId(UserThreadUtil.getHospitalId());
        question.setNumber(param.getNumber());
        question.setIsPublic(param.getIsPublic());
        question.setTitle(param.getTitle());
        question.setStatus(1);
        question.setTargetOne(param.getTargetOne());
        question.setTargetTwo(param.getTargetTwo());
        question.setTargetThree(param.getTargetThree());
        question.setOptionType(optionTemplate.getPid());
        question.setOptionMaxQuantity(param.getOptionMaxQuantity());
        question.setIsUseFace(param.getIsUseFace());
        question.setRemark(param.getRemark());
        question.setUpdateId(UserThreadUtil.getUserId());
        question.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        question.setCreateId(UserThreadUtil.getUserId());
        question.setCreateTime(new Timestamp(System.currentTimeMillis()));
        if (questionDao.insert(question)) {
            // 插入选项
            setQuestionOption(question.getId(), optionTemplate);
            // 插入标签
            setQuestionAttribute(question.getId(), param.getTagList());
        }
    }

    private void update(QuestionParam param) {
        Question data = new Question();
        data.setId(param.getId());
        data.setStatus(param.getStatus());
        if (StringUtil.isNotEmpty(param.getRemark())) {
            data.setRemark(param.getRemark());
        }
        data.setUpdateId(UserThreadUtil.getUserId());
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        questionService.updateById(data);
    }

    private void setQuestionOption(int questionId, OptionTemplate optionTemplate) {
        if (optionTemplate.getId().equals(98)) {
            QuestionOption data = new QuestionOption();
            data.setQuestionId(questionId);
            data.setTitle(optionTemplate.getContent());
            data.setSort(0);
            data.setScore(0);
            data.setTemplateId(optionTemplate.getId());
            questionOptionDao.insertQuestionOption(data);
        } else {
            String content = optionTemplate.getContent();
            String scoreString = optionTemplate.getScore();
            String[] split = content.split(",");
            String[] scoreArr = scoreString.split(",");
            int index = 1;
            for (String value : split) {
                QuestionOption data = new QuestionOption();
                data.setQuestionId(questionId);
                data.setTitle(value);
                data.setSort(index);
                if (split.length == scoreArr.length) {
                    data.setScore(Integer.parseInt(scoreArr[(index-1)]));
                }
                data.setTemplateId(optionTemplate.getId());
                questionOptionDao.insertQuestionOption(data);
                index++;
            }
        }
    }

    private void setQuestionAttribute(int questionId, List<TagParam> tagParam) {
        if (tagParam == null || tagParam.size() == 0) {
            return;
        }
        Wrapper<QuestionTagBinding> tagWrapper = new EntityWrapper<>();
        tagWrapper.eq(QuestionTagBinding.QUESTION_ID, questionId);
        questionTagBindingService.delete(tagWrapper);

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