package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.ReportAnswerOption;
import com.sunchs.lyt.db.business.entity.ReportItemScore;
import com.sunchs.lyt.db.business.service.impl.ReportAnswerOptionServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ReportItemScoreServiceImpl;
import com.sunchs.lyt.framework.bean.TitleData;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.report.bean.*;
import com.sunchs.lyt.report.service.IReportSingleOfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportSingleOfficeService implements IReportSingleOfficeService {

    @Autowired
    private ReportAnswerOptionServiceImpl reportAnswerOptionService;

    @Autowired
    private ReportItemScoreServiceImpl reportItemScoreService;

    @Override
    public SingleOfficeSatisfyData getItemSingleOfficeSatisfy(Integer itemId, Integer officeType, Integer officeId) {
        Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                .setSqlSelect(
                        ReportAnswerOption.ANSWER_ID.concat(" as answer_id"),
                        ReportAnswerOption.OFFICE_TYPE_ID.concat(" as officeTypeId"),
                        ReportAnswerOption.OFFICE_ID.concat(" as officeId"),
                        ReportAnswerOption.TARGET_THREE.concat(" as targetThree"),
                        ReportAnswerOption.QUESTION_ID.concat(" as questionId"),
                        ReportAnswerOption.OPTION_ID.concat(" as optionId"),
                        ReportAnswerOption.SCORE,
                        "COUNT(1) as quantity"
                )
                .eq(ReportAnswerOption.ITEM_ID, itemId)
                .eq(ReportAnswerOption.OFFICE_TYPE_ID, officeType)
                .in(ReportAnswerOption.OPTION_TYPE, Arrays.asList(1, 4))
                .ne(ReportAnswerOption.SCORE, 0)
                .groupBy(ReportAnswerOption.OFFICE_TYPE_ID)
                .groupBy(ReportAnswerOption.OFFICE_ID)
                .groupBy(ReportAnswerOption.QUESTION_ID)
                .groupBy(ReportAnswerOption.OPTION_ID);
        List<ReportAnswerOption> optionList = reportAnswerOptionService.selectList(wrapper);

        // 算出 当前科室 的信息（包含排名）
        CurrentOfficeBean info = getCurrentOfficeInfo(optionList, officeId);
        // 算出 所有科室 三级指标满意度
        List<TitleValueDataVO> itemTargetThreeScore = getItemTargetThreeScore(optionList);
        // 算出 当前科室 三级指标的排名
        List<TitleValueListVO> currentTargetThreeScore = getCurrentTargetThreeScore(optionList, officeId);
        // 获取当前科室的题目
//        currentTargetThreeScore.stream().map(TitleValueDataVO::getId)
        currentTargetThreeScore.forEach(cur->{

        });

        SingleOfficeSatisfyData data = new SingleOfficeSatisfyData();
        data.setOfficeSatisfyValue(info.getSatisfyValue());
        data.setAnswerQuantity(info.getAnswerQuantity());
        data.setLevelValue(info.getLevel());
//        data.setTitleList();
        data.setValueList(currentTargetThreeScore);



        return data;
    }

    @Override
    public void setItemOfficeRanking(Integer itemId) {
        new Thread(()->{
            execMakeItemOfficeScore(itemId);
        }).start();
    }

    public CurrentOfficeBean getCurrentOfficeInfo(List<ReportAnswerOption> optionList, Integer officeId) {
        CurrentOfficeBean bean = new CurrentOfficeBean();

        // 当前科室抽样量
        long answerQuantity = optionList.stream().map(ReportAnswerOption::getAnswerId).distinct().count();
        bean.setAnswerQuantity((int) answerQuantity);

        // 算排名
        List<TitleValueDataVO> rankingList = new ArrayList<>();
        // 根据科室ID划分
        Map<Integer, List<ReportAnswerOption>> officeGroup = optionList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getOfficeId));
        for (List<ReportAnswerOption> office : officeGroup.values()) {
            ReportAnswerOption officeRow = office.get(0);
            int officeNumber = 0;
            double officeScore = 0;
            // 计算每道题的得分
            Map<Integer, List<ReportAnswerOption>> questionGroup = office.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));
            for (List<ReportAnswerOption> option : questionGroup.values()) {
                int number = 0;
                int score = 0;
                // 累计
                for (ReportAnswerOption row : option) {
                    number += row.getQuantity();
                    score += row.getQuantity() * row.getScore();
                }
                // 满意度
                if (NumberUtil.nonZero(number)) {
                    double val = (double)score / (double)number;
                    officeScore += new BigDecimal(val).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    officeNumber++;
                }
            }
            // 科室得分
            double score = new BigDecimal(officeScore / (double) officeNumber).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            TitleValueDataVO t = new TitleValueDataVO();
            t.setId(officeRow.getOfficeId());
            t.setValue(score);
            rankingList.add(t);
        }

        // 当前科室满意度
        rankingList.sort(Comparator.comparing(TitleValueDataVO::getValue).reversed());
        // 排序次数过滤
        int rank = 0;
        double rankValue = 0;
        int tempRank = 0;
        for (TitleValueDataVO t : rankingList) {
            if (rank == 0) {
                rank++;
                rankValue = t.getValue();
                t.setRankValue(rank);
            } else if (rankValue != t.getValue()) {
                rank++;
                rankValue = t.getValue();
                rank += tempRank;
                tempRank = 0;
            } else {
                tempRank++;
            }
            t.setRankValue(rank);
        }
        Optional<TitleValueDataVO> voTemp = rankingList.stream().filter(v -> v.getId().equals(officeId)).findFirst();
        if (voTemp.isPresent()) {
            TitleValueDataVO vo = voTemp.get();
            bean.setLevel(vo.getRankValue());
            bean.setSatisfyValue(vo.getValue());
        }
        return bean;
    }

    private List<TitleValueListVO> getCurrentTargetThreeScore(List<ReportAnswerOption> optionList, Integer officeId) {
        List<TitleValueListVO> result = new ArrayList<>();
        List<ReportAnswerOption> collect = optionList.stream().filter(v -> v.getOfficeId().equals(officeId)).collect(Collectors.toList());
        // 按三级指标划分
        Map<Integer, List<ReportAnswerOption>> targetThreeGroup = collect.stream().collect(Collectors.groupingBy(ReportAnswerOption::getTargetThree));
        for (List<ReportAnswerOption> targetThree : targetThreeGroup.values()) {
            ReportAnswerOption targetThreeRow = targetThree.get(0);
            int targetNumber = 0;
            double targetScore = 0;
            // 计算每道题的得分
            List<SingleOfficeForQuestionList> questionList = new ArrayList<>();
            Map<Integer, List<ReportAnswerOption>> questionGroup = targetThree.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));
            for (Integer questionId : questionGroup.keySet()) {
                List<ReportAnswerOption> option = questionGroup.get(questionId);
                int number = 0;
                int score = 0;
                // 累计
                for (ReportAnswerOption row : option) {
                    number += row.getQuantity();
                    score += row.getQuantity() * row.getScore();
                }
                option.sort(Comparator.comparing(ReportAnswerOption::getScore).reversed());
                // 记录题目答题数量
                List<SingleOfficeRowValue> singleOfficeRowValueList = new ArrayList<>();
                int pos = 0;
                for (ReportAnswerOption reportAnswerOption : option) {
                    SingleOfficeRowValue singleOfficeRowValue = new SingleOfficeRowValue();
                    singleOfficeRowValue.setOptionId(reportAnswerOption.getOptionId());
                    pos++;
                    singleOfficeRowValue.setPosition(pos);
                    singleOfficeRowValue.setQuantity(reportAnswerOption.getQuantity());
                }
                // 满意度
                if (NumberUtil.nonZero(number)) {
                    double val = (double)score / (double)number;
                    targetScore += new BigDecimal(val).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    targetNumber++;
                }
                // 记录题目
                SingleOfficeForQuestionList singleOfficeForQuestionList = new SingleOfficeForQuestionList();
                singleOfficeForQuestionList.setQuestionId(questionId);
                singleOfficeForQuestionList.setOptionList(singleOfficeRowValueList);
                questionList.add(singleOfficeForQuestionList);
            }
            // 三级指标得分
            double score = new BigDecimal(targetScore / (double) targetNumber).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            // 保存数据
            TitleValueListVO data = new TitleValueListVO();
            data.setId(targetThreeRow.getTargetThree());
            data.setValue(score);
            data.setQuestionList(questionList);
            result.add(data);
        }
        return result;
    }

    private List<TitleValueDataVO> getItemTargetThreeScore(List<ReportAnswerOption> optionList) {
        List<TitleValueDataVO> result = new ArrayList<>();
        // 按三级指标划分
        Map<Integer, List<ReportAnswerOption>> targetThreeGroup = optionList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getTargetThree));
        for (List<ReportAnswerOption> targetThree : targetThreeGroup.values()) {
            ReportAnswerOption targetThreeRow = targetThree.get(0);
            int targetNumber = 0;
            double targetScore = 0;
            // 计算每道题的得分
            Map<Integer, List<ReportAnswerOption>> questionGroup = targetThree.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));
            for (List<ReportAnswerOption> option : questionGroup.values()) {
                int number = 0;
                int score = 0;
                // 累计
                for (ReportAnswerOption row : option) {
                    number += row.getQuantity();
                    score += row.getQuantity() * row.getScore();
                }
                option.sort(Comparator.comparing(ReportAnswerOption::getScore).reversed());

                // 满意度
                if (NumberUtil.nonZero(number)) {
                    double val = (double)score / (double)number;
                    targetScore += new BigDecimal(val).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    targetNumber++;
                }
            }
            // 三级指标得分
            double score = new BigDecimal(targetScore / (double) targetNumber).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            // 保存数据
            TitleValueDataVO data = new TitleValueDataVO();
            data.setId(targetThreeRow.getTargetThree());
            data.setValue(score);
            result.add(data);
        }
        return result;
    }

    private List<ReportItemScore> getItemOfficeScore(Integer itemId, List<ReportAnswerOption> optionList) {
        List<ReportItemScore> result = new ArrayList<>();

        // 根据科室类型分组
        Map<Integer, List<ReportAnswerOption>> officeTypeGroup = optionList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getOfficeTypeId));
        for (List<ReportAnswerOption> type : officeTypeGroup.values()) {
            ReportAnswerOption typeRow = type.get(0);
            // 根据科室ID划分
            Map<Integer, List<ReportAnswerOption>> officeGroup = type.stream().collect(Collectors.groupingBy(ReportAnswerOption::getOfficeId));
            for (List<ReportAnswerOption> office : officeGroup.values()) {
                ReportAnswerOption officeRow = office.get(0);
                int officeNumber = 0;
                double officeScore = 0;
                // 计算每道题的得分
                Map<Integer, List<ReportAnswerOption>> questionGroup = office.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));
                for (List<ReportAnswerOption> option : questionGroup.values()) {
                    int number = 0;
                    int score = 0;
                    // 累计
                    for (ReportAnswerOption row : option) {
                        number += row.getQuantity();
                        score += row.getQuantity() * row.getScore();
                    }
                    // 满意度
                    if (NumberUtil.nonZero(number)) {
                        double val = (double)score / (double)number;
                        officeScore += new BigDecimal(val).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        officeNumber++;
                    }
                }
                // 科室得分
                double score = new BigDecimal(officeScore / (double) officeNumber).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                // 保存数据
                ReportItemScore data = new ReportItemScore();
                data.setItemId(itemId);
                data.setOfficeTypeId(typeRow.getOfficeTypeId());
                data.setIdType(1);
                data.setIdValue(officeRow.getOfficeId());
                data.setScore(Float.parseFloat(score + ""));
                result.add(data);
//                reportItemScoreService.insert(data);
            }
        }

        for (List<ReportAnswerOption> type : officeTypeGroup.values()) {
            ReportAnswerOption typeRow = type.get(0);
            // 按三级指标划分
            Map<Integer, List<ReportAnswerOption>> targetThreeGroup = type.stream().collect(Collectors.groupingBy(ReportAnswerOption::getTargetThree));
            for (List<ReportAnswerOption> targetThree : targetThreeGroup.values()) {
                ReportAnswerOption targetThreeRow = targetThree.get(0);
                int targetNumber = 0;
                double targetScore = 0;
                // 计算每道题的得分
                Map<Integer, List<ReportAnswerOption>> questionGroup = targetThree.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));
                for (List<ReportAnswerOption> option : questionGroup.values()) {
                    int number = 0;
                    int score = 0;
                    // 累计
                    for (ReportAnswerOption row : option) {
                        number += row.getQuantity();
                        score += row.getQuantity() * row.getScore();
                    }
                    option.sort(Comparator.comparing(ReportAnswerOption::getScore).reversed());

                    // 满意度
                    if (NumberUtil.nonZero(number)) {
                        double val = (double)score / (double)number;
                        targetScore += new BigDecimal(val).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        targetNumber++;
                    }
                }
                // 三级指标得分
                double score = new BigDecimal(targetScore / (double) targetNumber).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                // 保存数据
                ReportItemScore data = new ReportItemScore();
                data.setItemId(itemId);
                data.setOfficeTypeId(typeRow.getOfficeTypeId());
                data.setIdType(2);
                data.setIdValue(targetThreeRow.getTargetThree());
                data.setScore(Float.parseFloat(score + ""));
                result.add(data);
//                reportItemScoreService.insert(data);
            }
        }
        return result;
    }

    private void execMakeItemOfficeScore(Integer itemId) {
        Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                .setSqlSelect(
                        ReportAnswerOption.OFFICE_TYPE_ID.concat(" as officeTypeId"),
                        ReportAnswerOption.OFFICE_ID.concat(" as officeId"),
                        ReportAnswerOption.TARGET_THREE.concat(" as targetThree"),
                        ReportAnswerOption.QUESTION_ID.concat(" as questionId"),
                        ReportAnswerOption.OPTION_ID.concat(" as optionId"),
                        ReportAnswerOption.SCORE,
                        "COUNT(1) as quantity"
                )
                .eq(ReportAnswerOption.ITEM_ID, itemId)
                .in(ReportAnswerOption.OPTION_TYPE, Arrays.asList(1, 4))
                .ne(ReportAnswerOption.SCORE, 0)
                .groupBy(ReportAnswerOption.OFFICE_TYPE_ID)
                .groupBy(ReportAnswerOption.OFFICE_ID)
                .groupBy(ReportAnswerOption.QUESTION_ID)
                .groupBy(ReportAnswerOption.OPTION_ID);
        List<ReportAnswerOption> optionList = reportAnswerOptionService.selectList(wrapper);

        // 清理历史数据
        Wrapper<ReportItemScore> reportItemScoreWrapper = new EntityWrapper<ReportItemScore>()
                .eq(ReportItemScore.ITEM_ID, itemId);
        reportItemScoreService.delete(reportItemScoreWrapper);

        // 根据科室类型分组
        Map<Integer, List<ReportAnswerOption>> officeTypeGroup = optionList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getOfficeTypeId));
        for (List<ReportAnswerOption> type : officeTypeGroup.values()) {
            ReportAnswerOption typeRow = type.get(0);
            // 根据科室ID划分
            Map<Integer, List<ReportAnswerOption>> officeGroup = type.stream().collect(Collectors.groupingBy(ReportAnswerOption::getOfficeId));
            for (List<ReportAnswerOption> office : officeGroup.values()) {
                ReportAnswerOption officeRow = office.get(0);
                int officeNumber = 0;
                double officeScore = 0;
                // 计算每道题的得分
                Map<Integer, List<ReportAnswerOption>> questionGroup = office.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));
                for (List<ReportAnswerOption> option : questionGroup.values()) {
                    int number = 0;
                    int score = 0;
                    // 累计
                    for (ReportAnswerOption row : option) {
                        number += row.getQuantity();
                        score += row.getQuantity() * row.getScore();
                    }
                    // 满意度
                    if (NumberUtil.nonZero(number)) {
                        double val = (double)score / (double)number;
                        officeScore += new BigDecimal(val).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        officeNumber++;
                    }
                }
                // 科室得分
                double score = new BigDecimal(officeScore / (double) officeNumber).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                // 保存数据
                ReportItemScore data = new ReportItemScore();
                data.setItemId(itemId);
                data.setOfficeTypeId(typeRow.getOfficeTypeId());
                data.setIdType(1);
                data.setIdValue(officeRow.getOfficeId());
                data.setScore(Float.parseFloat(score + ""));
                reportItemScoreService.insert(data);
            }
        }

        for (List<ReportAnswerOption> type : officeTypeGroup.values()) {
            ReportAnswerOption typeRow = type.get(0);
            // 按三级指标划分
            Map<Integer, List<ReportAnswerOption>> targetThreeGroup = type.stream().collect(Collectors.groupingBy(ReportAnswerOption::getTargetThree));
            for (List<ReportAnswerOption> targetThree : targetThreeGroup.values()) {
                ReportAnswerOption targetThreeRow = targetThree.get(0);
                int targetNumber = 0;
                double targetScore = 0;
                // 计算每道题的得分
                Map<Integer, List<ReportAnswerOption>> questionGroup = targetThree.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));
                for (List<ReportAnswerOption> option : questionGroup.values()) {
                    int number = 0;
                    int score = 0;
                    // 累计
                    for (ReportAnswerOption row : option) {
                        number += row.getQuantity();
                        score += row.getQuantity() * row.getScore();
                    }
                    // 满意度
                    if (NumberUtil.nonZero(number)) {
                        double val = (double)score / (double)number;
                        targetScore += new BigDecimal(val).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        targetNumber++;
                    }
                }
                // 三级指标得分
                double score = new BigDecimal(targetScore / (double) targetNumber).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                // 保存数据
                ReportItemScore data = new ReportItemScore();
                data.setItemId(itemId);
                data.setOfficeTypeId(typeRow.getOfficeTypeId());
                data.setIdType(2);
                data.setIdValue(targetThreeRow.getTargetThree());
                data.setScore(Float.parseFloat(score + ""));
                reportItemScoreService.insert(data);
            }
        }
    }
}
