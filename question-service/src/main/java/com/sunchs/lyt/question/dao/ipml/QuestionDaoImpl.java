package com.sunchs.lyt.question.dao.ipml;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.Question;
import com.sunchs.lyt.db.business.service.impl.QuestionServiceImpl;
import com.sunchs.lyt.framework.util.FormatUtil;
import com.sunchs.lyt.framework.util.ObjectUtil;
import com.sunchs.lyt.question.bean.OptionData;
import com.sunchs.lyt.question.bean.QuestionData;
import com.sunchs.lyt.question.bean.QuestionOptionData;
import com.sunchs.lyt.question.bean.TagData;
import com.sunchs.lyt.question.dao.QuestionDao;
import com.sunchs.lyt.question.dao.QuestionTargetDao;
import com.sunchs.lyt.question.enums.QuestionStatusEnum;
import com.sunchs.lyt.question.exception.QuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class QuestionDaoImpl implements QuestionDao {

    @Autowired
    private QuestionOptionDaoImpl questionOptionDao;

    @Autowired
    private QuestionTargetDao questionTargetDao;

    @Autowired
    private QuestionTagDaoImpl questionTagDao;

    @Autowired
    private QuestionServiceImpl questionService;

    @Override
    public QuestionData getById(int questionId) {
        Wrapper<Question> where = new EntityWrapper<>();
        where.eq("id", questionId);
        Question question = questionService.selectOne(where);
        if (Objects.nonNull(question)) {
            QuestionData data = ObjectUtil.copy(question, QuestionData.class);
            data.setStatusName(QuestionStatusEnum.get(data.getStatus()));
            data.setTargetOneName(questionTargetDao.getNameById(data.getTargetOne()));
            data.setTargetTwoName(questionTargetDao.getNameById(data.getTargetTwo()));
            data.setTargetThreeName(questionTargetDao.getNameById(data.getTargetThree()));

            QuestionOptionData optionData = questionOptionDao.getInfo(data.getOptionType());
            data.setOptionTypeName(optionData.getTitle());
            data.setOptionMode(optionData.getMode());
            List<OptionData> optionDataList = questionOptionDao.getListById(data.getId());
            data.setOptionList(optionDataList);
            data.setOptionListName("");
            for (int i = 0; i < optionDataList.size(); i++) {
                data.setOptionListName(data.getOptionListName() + (i+1) + "、" + optionDataList.get(i).getOptionName() + "  ");
            }

            // 标签列表
            List<TagData> tagDataList = questionTagDao.getQuestionTag(questionId);
            data.setTagList(tagDataList);
            data.setTagListName("");
            for (int i = 0; i < tagDataList.size(); i++) {
                data.setTagListName(data.getTagListName() + (i+1) + "、" + tagDataList.get(i).getTagName() + "  ");
            }

            data.setUpdateTimeName(FormatUtil.dateTime(data.getUpdateTime()));

            return data;
        }
        return null;
    }

    @Override
    public Page<Question> getPaging(Wrapper<Question> where, int pageNow, int pageSize) {
        Page<Question> limit = new Page<>(pageNow, pageSize);
        return questionService.selectPage(limit, where);
    }

    @Override
    public boolean insert(Question question) {
        try {
            return questionService.insert(question);
        } catch (Exception e) {
            throw new QuestionException("添加问题数据 --> 异常:" + e.getMessage());
        }
    }

    @Override
    public boolean update(Question question) {
        try {
            return questionService.updateById(question);
        } catch (Exception e) {
            throw new QuestionException("修改问题状态 --> 异常:" + e.getMessage());
        }
    }
}