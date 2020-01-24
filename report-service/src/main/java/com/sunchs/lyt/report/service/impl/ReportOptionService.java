package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.QuestionOption;
import com.sunchs.lyt.db.business.entity.ReportAnswerOption;
import com.sunchs.lyt.db.business.service.impl.QuestionOptionServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ReportAnswerOptionServiceImpl;
import com.sunchs.lyt.framework.bean.TitleValueData;
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
                }
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
