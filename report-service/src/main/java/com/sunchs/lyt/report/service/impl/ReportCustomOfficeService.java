package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.*;
import com.sunchs.lyt.db.business.service.impl.*;
import com.sunchs.lyt.framework.bean.TitleValueData;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.report.bean.CustomOfficeData;
import com.sunchs.lyt.report.bean.CustomOfficeDataVO;
import com.sunchs.lyt.report.bean.CustomOfficeTargetData;
import com.sunchs.lyt.report.service.IReportCustomOfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportCustomOfficeService implements IReportCustomOfficeService {

    @Autowired
    private CustomItemOfficeServiceImpl customItemOfficeService;

    @Autowired
    private CustomItemTargetServiceImpl customItemTargetService;

    @Autowired
    private ReportAnswerSatisfyServiceImpl reportAnswerSatisfyService;

    @Autowired
    private ReportAnswerOptionServiceImpl reportAnswerOptionService;

    @Autowired
    private QuestionTargetServiceImpl questionTargetService;

    @Override
    public CustomOfficeDataVO getCustomOfficeList(Integer itemId, Integer officeType) {
        // 列表集合
        List<CustomOfficeData> list =  new ArrayList<>();
        Wrapper<CustomItemOffice> wrapper = new EntityWrapper<CustomItemOffice>()
                .eq(CustomItemOffice.ITEM_ID, itemId)
                .eq(CustomItemOffice.OFFICE_TYPE, officeType);
        List<CustomItemOffice> officeList = customItemOfficeService.selectList(wrapper);
        officeList.forEach(o->{
            CustomOfficeData data = new CustomOfficeData();
            data.setId(o.getId());
            data.setTitle(o.getTitle());
            // 获取答卷ID集合
            Wrapper<ReportAnswerOption> optionWrapper = new EntityWrapper<ReportAnswerOption>()
                    .setSqlSelect(ReportAnswerOption.ANSWER_ID+" as answerId")
                    .eq(ReportAnswerOption.ITEM_ID, itemId)
                    .eq(ReportAnswerOption.OFFICE_TYPE_ID, o.getOfficeType())
                    .eq(ReportAnswerOption.QUESTIONNAIRE_ID, o.getQuestionnaireId())
                    .eq(ReportAnswerOption.QUESTION_ID, o.getQuestionId())
                    .eq(ReportAnswerOption.OPTION_ID, o.getOptionId())
                    .groupBy(ReportAnswerOption.ANSWER_ID);
            List<ReportAnswerOption> optionTempList = reportAnswerOptionService.selectList(optionWrapper);
            List<Integer> answerIds = optionTempList.stream().map(ReportAnswerOption::getAnswerId).collect(Collectors.toList());
            data.setTargetList(getTargetSatisfyList(answerIds, o.getId()));
            list.add(data);
        });
        CustomOfficeDataVO vo = new CustomOfficeDataVO();
        vo.setList(list);
        // 排名
        List<TitleValueData> rankingList = new ArrayList<>();
        list.forEach(o->{
            double allScore = 0;
            int number = 0;
            List<CustomOfficeTargetData> targetList = o.getTargetList();
            for (CustomOfficeTargetData t : targetList) {
                if (t.getSatisfyValue() > 0) {
                    allScore += t.getSatisfyValue();
                    number++;
                }
            }
            double value = new BigDecimal(allScore / (double)number).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            TitleValueData d = new TitleValueData();
            d.setId(o.getId());
            d.setTitle(o.getTitle());
            d.setValue(value);
            rankingList.add(d);
        });
        rankingList.sort(Comparator.comparing(TitleValueData::getValue).reversed());
        vo.setRankingList(rankingList);
        return vo;
    }

    private List<CustomOfficeTargetData> getTargetSatisfyList(List<Integer> answerIds, Integer customId) {
        List<CustomOfficeTargetData> result = new ArrayList<>();
        Wrapper<CustomItemTarget> wrapper = new EntityWrapper<CustomItemTarget>()
                .eq(CustomItemTarget.CUSTOM_ID, customId);
        List<CustomItemTarget> targetList = customItemTargetService.selectList(wrapper);
        List<Integer> targetIds = targetList.stream().map(CustomItemTarget::getTargetThree).collect(Collectors.toList());
        List<ReportAnswerOption> targetQuestionList = getTargetQuestionList(answerIds, targetIds);
        Map<Integer, String> targetNameMap = getTargetNameMap(targetIds);
        targetList.forEach(target->{
            CustomOfficeTargetData data = new CustomOfficeTargetData();
            data.setTargetId(target.getTargetThree());
            data.setTargetTitle(targetNameMap.get(target.getTargetThree()));

            // 计算满意度
            List<ReportAnswerOption> optionTempList = targetQuestionList.stream()
                    .filter(q -> q.getTargetThree().equals(target.getTargetThree())).collect(Collectors.toList());
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
                double value = new BigDecimal((double)allScore / (double)scoreList.size() / (double)100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                data.setSatisfyValue(value);
            }
            result.add(data);
        });
        return result;
    }

    private List<ReportAnswerOption> getTargetQuestionList(List<Integer> answerIds, List<Integer> targetIds) {
        // 获取指标题目
        Wrapper<ReportAnswerOption> targetWrapper = new EntityWrapper<ReportAnswerOption>()
                .setSqlSelect("COUNT(1) quantity",
                        ReportAnswerOption.QUESTION_ID + " as questionId",
                        ReportAnswerOption.OPTION_ID + " as optionId",
                        ReportAnswerOption.SCORE,
                        ReportAnswerOption.TARGET_ONE + " as targetOne",
                        ReportAnswerOption.TARGET_THREE + " as targetThree")
                .in(ReportAnswerOption.ANSWER_ID, answerIds)
                .in(ReportAnswerOption.TARGET_THREE, targetIds)
                .in(ReportAnswerOption.OPTION_TYPE, new Integer[]{1,4})
                .groupBy(ReportAnswerOption.QUESTIONNAIRE_ID)
                .groupBy(ReportAnswerOption.QUESTION_ID)
                .groupBy(ReportAnswerOption.OPTION_ID);
        List<ReportAnswerOption> optionList = reportAnswerOptionService.selectList(targetWrapper);
        System.out.println(targetWrapper.getSqlSegment());
        return optionList;
    }

    /**
     * 根据 指标ID集合 获取名称
     */
    private Map<Integer, String> getTargetNameMap(List<Integer> targetIds) {
        Map<Integer, String> map = new HashMap<>();
        Wrapper<QuestionTarget> wrapper = new EntityWrapper<QuestionTarget>()
                .setSqlSelect(QuestionTarget.ID, QuestionTarget.TITLE)
                .in(QuestionTarget.ID, targetIds);
        List<QuestionTarget> targetList = questionTargetService.selectList(wrapper);
        targetList.forEach(target->{
            map.put(target.getId(), target.getTitle());
        });
        return map;
    }
}
