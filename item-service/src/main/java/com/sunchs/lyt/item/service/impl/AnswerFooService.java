package com.sunchs.lyt.item.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.*;
import com.sunchs.lyt.db.business.service.impl.*;
import com.sunchs.lyt.framework.constants.CacheKeys;
import com.sunchs.lyt.framework.util.*;
import com.sunchs.lyt.item.bean.AnswerParam;
import com.sunchs.lyt.item.bean.ItemOfficeFooData;
import com.sunchs.lyt.item.bean.SyncAnswerParam;
import com.sunchs.lyt.item.enums.OfficeTypeEnum;
import com.sunchs.lyt.item.exception.ItemException;
import com.sunchs.lyt.item.service.IAnswerFooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class AnswerFooService implements IAnswerFooService {

    @Autowired
    private ItemOfficeServiceImpl itemOfficeService;

    @Autowired
    private HospitalOfficeServiceImpl hospitalOfficeService;

    @Autowired
    private QuestionnaireServiceImpl questionnaireService;

    @Autowired
    private HospitalServiceImpl hospitalService;

    @Autowired
    private AnswerServiceImpl answerService;

    @Autowired
    private QuestionnaireExtendServiceImpl questionnaireExtendService;

    @Autowired
    private QuestionServiceImpl questionService;

    @Autowired
    private QuestionOptionServiceImpl questionOptionService;

    @Autowired
    private AnswerOptionServiceImpl answerOptionService;

    @Override
    public void saveAnswer(SyncAnswerParam param) {
        // 检查重复
        checkAnswer(param);

        // 获取项目相关信息
        ItemOffice itemOffice = getItemOffice(param.getItemId(), param.getOfficeId());

        Wrapper<Answer> answerWrapper = new EntityWrapper<Answer>()
                .orderBy(Answer.ID, false);
        Answer last = answerService.selectOne(answerWrapper);

        // 插入答卷
        Answer data = new Answer();
        data.setHospitalId(itemOffice.getHospitalId());
        data.setItemId(param.getItemId());
        data.setOfficeTypeId(itemOffice.getOfficeTypeId());
        data.setOfficeId(param.getOfficeId());
        data.setQuestionnaireId(itemOffice.getQuestionnaireId());
        data.setUserId(UserThreadUtil.getUserId());
        data.setMemberId(param.getMemberId());
        data.setPatientNumber(param.getPatientNumber());
        data.setStatus(0);
        data.setReason("");
        data.setTimeDuration(param.getTimeDuration());
        data.setStartTime(FormatUtil.dateTime(param.getStartTime()));
        data.setEndTime(FormatUtil.dateTime(param.getEndTime()));
        data.setUpdateId(-1);
        data.setUpdateTime(new Date());
        data.setCreateId(-1);
        data.setCreateTime(new Date());

        data.setFilterReason("");
        if (Objects.nonNull(last)) {
            long tVal = data.getStartTime().getTime() - last.getEndTime().getTime();
            if (tVal < 20 * 1000) {
                data.setFilterReason("距离上次答卷时间太短");
            }
        }

        if (answerService.insert(data)) {
            new Thread(()->{
                // 插入答题
                param.getQuestionList().forEach(q -> {
                    // 检查题目
                    boolean isExist = answerQuestionIsExist(data.getQuestionnaireId(), q.getQuestionId());
                    Question question = getQuestionById(q.getQuestionId());
                    if (isExist && Objects.nonNull(question)) {
                        if (q.getOptionMode().equals("text")) {
                            AnswerOption answerOption = new AnswerOption();
                            answerOption.setAnswerId(data.getId());
                            answerOption.setItemId(param.getItemId());
                            answerOption.setOfficeTypeId(data.getOfficeTypeId());
                            answerOption.setOfficeId(data.getOfficeId());
                            answerOption.setQuestionnaireId(data.getQuestionnaireId());
                            answerOption.setQuestionId(q.getQuestionId());
                            answerOption.setQuestionName(question.getTitle());
                            answerOption.setOptionId(0);
                            answerOption.setOptionName(q.getOptionValue());
                            answerOption.setTimeDuration(q.getTimeDuration());
                            answerOption.setStartTime(FormatUtil.dateTime(q.getStartTime()));
                            answerOption.setEndTime(FormatUtil.dateTime(q.getEndTime()));
                            answerOption.setTargetOne(question.getTargetOne());
                            answerOption.setTargetTwo(question.getTargetTwo());
                            answerOption.setTargetThree(question.getTargetThree());
                            answerOption.setOptionType(question.getOptionType());
                            answerOption.setScore(0);
                            answerOptionService.insert(answerOption);
                        } else {
                            q.getOptionIds().forEach(optionId -> {
                                QuestionOption option = getQuestionOption(optionId);
                                if (Objects.nonNull(option) && option.getQuestionId().equals(q.getQuestionId())) {
                                    AnswerOption answerOption = new AnswerOption();
                                    answerOption.setAnswerId(data.getId());
                                    answerOption.setItemId(param.getItemId());
                                    answerOption.setOfficeTypeId(data.getOfficeTypeId());
                                    answerOption.setOfficeId(data.getOfficeId());
                                    answerOption.setQuestionnaireId(data.getQuestionnaireId());
                                    answerOption.setQuestionId(q.getQuestionId());
                                    answerOption.setQuestionName(question.getTitle());
//                    answerOption.setQuestionName(q.getQuestionName());
                                    answerOption.setOptionId(optionId);
                                    answerOption.setOptionName(option.getTitle());
                                    answerOption.setTimeDuration(q.getTimeDuration());
                                    answerOption.setStartTime(FormatUtil.dateTime(q.getStartTime()));
                                    answerOption.setEndTime(FormatUtil.dateTime(q.getEndTime()));
//                    answerOption.setOptionName(q.getOptionName());
                                    answerOption.setTargetOne(question.getTargetOne());
                                    answerOption.setTargetTwo(question.getTargetTwo());
                                    answerOption.setTargetThree(question.getTargetThree());
                                    answerOption.setOptionType(question.getOptionType());
                                    answerOption.setScore(option.getScore());
                                    answerOptionService.insert(answerOption);
                                } else {
                                    System.out.println("同步参数有误");
                                }
                            });
                        }
                    }
                });
            }).start();
        }
    }

    @Override
    public ItemOfficeFooData getItemOfficeInfo(int itemId, int officeId) {
        ItemOffice itemOffice = getItemOffice(itemId, officeId);
        if (Objects.isNull(itemOffice)) {
            throw new ItemException("项目科室已停用！");
        }

        ItemOfficeFooData data = ObjectUtil.copy(itemOffice, ItemOfficeFooData.class);
        data.setHospitalName(getHospitalNameById(data.getHospitalId()));
        data.setOfficeName(getOfficeNameById(data.getOfficeId()));
        data.setOfficeTypeName(OfficeTypeEnum.get(data.getOfficeTypeId()));
        data.setQuestionnaireName(getQuestionnaireNameById(data.getQuestionnaireId()));

        return data;
    }

    private boolean answerQuestionIsExist(int questionnaireId, int questionId) {
        Wrapper<QuestionnaireExtend> wrapper = new EntityWrapper<QuestionnaireExtend>()
                .eq(QuestionnaireExtend.QUESTIONNAIRE_ID, questionnaireId)
                .eq(QuestionnaireExtend.QUESTION_ID, questionId);
        int count = questionnaireExtendService.selectCount(wrapper);
        return (count > 0);
    }

    private void checkAnswer(SyncAnswerParam param) {
        if ( ! RedisUtil.setnx(CacheKeys.PATIENT_NUMBER + param.getPatientNumber(), "1", 60 * 10)) {
            throw new ItemException("答卷无需重复上传");
        }
        Wrapper<Answer> wrapper = new EntityWrapper<Answer>()
                .eq(Answer.ITEM_ID, param.getItemId())
                .eq(Answer.OFFICE_ID, param.getOfficeId())
                .eq(Answer.PATIENT_NUMBER, param.getPatientNumber());
        int count = answerService.selectCount(wrapper);
        if (count > 0) {
            throw new ItemException("答卷无需重复上传");
        }
    }

    private ItemOffice getItemOffice(int itemId, int officeId) {
        Wrapper<ItemOffice> wrapper = new EntityWrapper<ItemOffice>()
                .eq(ItemOffice.ITEM_ID, itemId)
                .eq(ItemOffice.OFFICE_ID, officeId);
        return itemOfficeService.selectOne(wrapper);
    }

    private String getHospitalNameById(int hospitalId) {
        Wrapper<Hospital> wrapper = new EntityWrapper<Hospital>()
                .eq(Hospital.ID, hospitalId);
        Hospital row = hospitalService.selectOne(wrapper);
        if (Objects.nonNull(row)) {
            return row.getHospitalName();
        }
        return "";
    }

    private String getOfficeNameById(int officeId) {
        Wrapper<HospitalOffice> wrapper = new EntityWrapper<HospitalOffice>()
                .eq(HospitalOffice.ID, officeId);
        HospitalOffice row = hospitalOfficeService.selectOne(wrapper);
        if (Objects.nonNull(row)) {
            return row.getTitle();
        }
        return "";
    }

    private String getQuestionnaireNameById(int questionnaireId) {
        Wrapper<Questionnaire> wrapper = new EntityWrapper<Questionnaire>()
                .eq(Questionnaire.ID, questionnaireId);
        Questionnaire row = questionnaireService.selectOne(wrapper);
        if (Objects.nonNull(row)) {
            return row.getTitle();
        }
        return "";
    }

    private Question getQuestionById(int questionId) {
        Wrapper<Question> wrapper = new EntityWrapper<Question>()
                .eq(Question.ID, questionId);
        return questionService.selectOne(wrapper);
    }

    private QuestionOption getQuestionOption(int optionId) {
        Wrapper<QuestionOption> wrapper = new EntityWrapper<QuestionOption>()
                .eq(QuestionOption.ID, optionId);
        return questionOptionService.selectOne(wrapper);
    }
}
