package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.QuestionOption;
import com.sunchs.lyt.db.business.entity.ReportAnswerOption;
import com.sunchs.lyt.db.business.service.impl.QuestionOptionServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ReportAnswerOptionServiceImpl;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.report.bean.ItemCrowdParam;
import com.sunchs.lyt.report.bean.SatisfyData;
import com.sunchs.lyt.report.service.IReportOptionService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportOptionService implements IReportOptionService {

    @Autowired
    private ReportAnswerOptionServiceImpl reportAnswerOptionService;

    @Autowired
    private QuestionOptionServiceImpl questionOptionService;

    @Override
    public List<SatisfyData> getItemCrowdSatisfy(ItemCrowdParam param) {
        List<SatisfyData> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(param.getOptionIds())) {
            return result;
        }
        Map<Integer, QuestionOption> questionOptionMap = getQuestionOptionMap(param.getOptionIds());
        Map<Integer, List<ReportAnswerOption>> questionGroup = getOptionQuestionIds(param.getItemId(), param.getOfficeType(), param.getOptionIds());
        param.getOptionIds().forEach(optionId -> {
            if (questionGroup.containsKey(optionId)) {
                List<ReportAnswerOption> questionList = questionGroup.get(optionId);
                List<Integer> questionIds = questionList.stream().map(ReportAnswerOption::getQuestionId).collect(Collectors.toList());
                // 计算满意度
                double satisfyValue = getSatisfyValue(param.getItemId(), param.getOfficeType(), questionIds);
                // 增加记录
                QuestionOption questionOption = questionOptionMap.get(optionId);
                if (Objects.nonNull(questionOption)) {
                    SatisfyData row = new SatisfyData();
                    row.setpId(questionOption.getQuestionId());
                    row.setId(optionId);
                    row.setName(questionOption.getTitle());
                    row.setValue(satisfyValue);
                    result.add(row);
                }
            }
        });
        return result;
    }

    @Override
    public List<SatisfyData> getItemCrowdAnswerSatisfy(ItemCrowdParam param) {
        List<SatisfyData> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(param.getOptionIds())) {
            return result;
        }
        Map<Integer, QuestionOption> questionOptionMap = getQuestionOptionMap(param.getOptionIds());
        // 获取所有答卷ID集合，按optionId分好组
        Map<Integer, List<ReportAnswerOption>> questionGroup = getOptionAnswerIds(param);
        param.getOptionIds().forEach(optionId -> {
            if (questionGroup.containsKey(optionId)) {
                List<ReportAnswerOption> answerList = questionGroup.get(optionId);
                List<Integer> answerIds = answerList.stream().map(ReportAnswerOption::getAnswerId).collect(Collectors.toList());
                System.out.println(optionId+":"+answerIds+"--->\r\n");
                if (answerIds.size() > 0) {
                    Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                            .setSqlSelect("question_id AS questionId,option_id AS optionId,score,COUNT(1) quantity")
                            .in(ReportAnswerOption.ANSWER_ID, answerIds)
                            .in(ReportAnswerOption.OPTION_TYPE, Arrays.asList(1, 4))
                            .groupBy(ReportAnswerOption.QUESTION_ID)
                            .groupBy(ReportAnswerOption.OPTION_ID);
                    List<ReportAnswerOption> answerOptionList = reportAnswerOptionService.selectList(wrapper);
                    if (CollectionUtils.isNotEmpty(answerOptionList)) {
                        // 计算满意度
                        double allVal = 0;
                        Map<Integer, List<ReportAnswerOption>> questionMap = answerOptionList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));
                        for (Integer questionId : questionMap.keySet()) {
                            List<ReportAnswerOption> optionList = questionMap.get(questionId);
                            // 计算满意度
                            double value = 0;
                            int number = 0;
                            for (ReportAnswerOption option : optionList) {
                                if ( ! option.getScore().equals(0)) {
                                    value += option.getScore().doubleValue() * option.getQuantity().doubleValue();
                                    number += option.getQuantity().intValue();
                                }
                            }
                            if (number > 0) {
                                allVal += new BigDecimal(value / (double) number).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            }
                        }
                        // 赋值
                        SatisfyData data = new SatisfyData();
                        data.setId(optionId);
                        if (questionMap.size() > 0) {
                            data.setValue(new BigDecimal(allVal / (double) questionMap.size()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        } else {
                            data.setValue(0.00);
                        }
                        QuestionOption questionOption = questionOptionMap.get(optionId);
                        data.setName(questionOption.getTitle());
                        data.setpId(questionOption.getQuestionId());
                        result.add(data);
                    }
                }
            } else {
                // 无内容时，赋值
                SatisfyData data = new SatisfyData();
                data.setId(optionId);
                data.setValue(0.00);
                QuestionOption questionOption = questionOptionMap.get(optionId);
                data.setName(questionOption.getTitle());
                data.setpId(questionOption.getQuestionId());
                result.add(data);
            }
        });
        return result;
    }

    /**
     * 根据 选项ID集合 获取对应的题目ID集合（已分组好）
     */
    private Map<Integer, List<ReportAnswerOption>> getOptionQuestionIds(Integer itemId, Integer officeType, List<Integer> optionIds) {
        Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                .setSqlSelect(
                        ReportAnswerOption.QUESTION_ID.concat(" as questionId"),
                        ReportAnswerOption.OPTION_ID.concat(" as optionId")
                )
                .eq(ReportAnswerOption.ITEM_ID, itemId)
                .eq(ReportAnswerOption.OFFICE_TYPE_ID, officeType)
                .in(ReportAnswerOption.OPTION_TYPE, Arrays.asList(1, 4))
                .in(ReportAnswerOption.OPTION_ID, optionIds);
        List<ReportAnswerOption> reportAnswerOptions = reportAnswerOptionService.selectList(wrapper);
        return reportAnswerOptions.stream().collect(Collectors.groupingBy(ReportAnswerOption::getOptionId));
    }

    /**
     * 根据 选项ID集合 获取对应的答卷ID集合（已分组好）
     */
    private Map<Integer, List<ReportAnswerOption>> getOptionAnswerIds(ItemCrowdParam param) {
        Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                .setSqlSelect(
                        ReportAnswerOption.ANSWER_ID.concat(" as answerId"),
                        ReportAnswerOption.OPTION_ID.concat(" as optionId")
                )
                .eq(ReportAnswerOption.ITEM_ID, param.getItemId())
                .eq(ReportAnswerOption.OFFICE_TYPE_ID, param.getOfficeType())
                .in(ReportAnswerOption.OPTION_ID, param.getOptionIds())
                .groupBy(ReportAnswerOption.ANSWER_ID);
        // 根据时间段
        if (Objects.nonNull(param.getStartTime()) && param.getStartTime().length() > 0) {
            wrapper.ge(ReportAnswerOption.ENDTIME, param.getStartTime())
                    .le(ReportAnswerOption.ENDTIME, param.getEndTime());
        }
        List<ReportAnswerOption> reportAnswerOptions = reportAnswerOptionService.selectList(wrapper);
        return reportAnswerOptions.stream().collect(Collectors.groupingBy(ReportAnswerOption::getOptionId));
    }

    /**
     * 根据 题目ID集合 获取满意度
     */
    private double getSatisfyValue(Integer itemId, Integer officeType, List<Integer> questionIds) {
        Wrapper<ReportAnswerOption> targetWrapper = new EntityWrapper<ReportAnswerOption>()
                .setSqlSelect(
                        "COUNT(1) quantity",
                        ReportAnswerOption.QUESTION_ID + " as questionId",
                        ReportAnswerOption.OPTION_ID + " as optionId",
                        ReportAnswerOption.SCORE
                )
                .eq(ReportAnswerOption.ITEM_ID, itemId)
                .eq(ReportAnswerOption.OFFICE_TYPE_ID, officeType)
                .in(ReportAnswerOption.QUESTION_ID, questionIds)
                .in(ReportAnswerOption.OPTION_TYPE, Arrays.asList(1, 4))
                .groupBy(ReportAnswerOption.QUESTION_ID)
                .groupBy(ReportAnswerOption.OPTION_ID);
        List<ReportAnswerOption> optionTempList = reportAnswerOptionService.selectList(targetWrapper);
        // 计算满意度
        Map<Integer, List<ReportAnswerOption>> qList = optionTempList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));
        List<Integer> scoreList = new ArrayList<>();
        for (List<ReportAnswerOption> questionGroup : qList.values()) {
            // 累计
            int number = 0;
            int score = 0;
            for (ReportAnswerOption row : questionGroup) {
                if (row.getScore() > 0) {
                    number += row.getQuantity();
                    score += row.getQuantity() * row.getScore();
                }
            }
            // 满意度
            if (NumberUtil.nonZero(number)) {
                int val = (int)(score / number * 100);
                scoreList.add(val);
            }
        }
        if (scoreList.size() > 0) {
            int allScore = 0;
            for (Integer lScore : scoreList) {
                allScore += lScore;
            }
            return new BigDecimal((double)allScore / (double)scoreList.size() / (double)100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return 0.00;
    }

    private Map<Integer, QuestionOption> getQuestionOptionMap(List<Integer> optionIds) {
        Map<Integer, QuestionOption> map = new HashMap<>();
        Wrapper<QuestionOption> wrapper = new EntityWrapper<QuestionOption>()
                .in(QuestionOption.ID, optionIds);
        List<QuestionOption> questionOptionList = questionOptionService.selectList(wrapper);
        questionOptionList.forEach(o -> map.put(o.getId(), o));
        return map;
    }
}
