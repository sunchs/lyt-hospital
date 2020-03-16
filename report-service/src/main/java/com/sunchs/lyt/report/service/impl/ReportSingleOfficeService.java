package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.Question;
import com.sunchs.lyt.db.business.entity.ReportAnswerOption;
import com.sunchs.lyt.db.business.entity.ReportItemScore;
import com.sunchs.lyt.db.business.service.impl.QuestionServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ReportAnswerOptionServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ReportItemScoreServiceImpl;
import com.sunchs.lyt.framework.bean.TitleData;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.report.bean.*;
import com.sunchs.lyt.report.service.IReportSingleOfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.dc.pr.PRError;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportSingleOfficeService implements IReportSingleOfficeService {

    @Autowired
    private ReportAnswerOptionServiceImpl reportAnswerOptionService;

    @Autowired
    private ReportItemScoreServiceImpl reportItemScoreService;

    @Autowired
    private QuestionServiceImpl questionService;

    @Override
    public SingleOfficeSatisfyData getItemSingleOfficeSatisfy(Integer itemId, Integer officeType, Integer officeId) {
        Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                .setSqlSelect(
                        ReportAnswerOption.ANSWER_ID.concat(" as answerId"),
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
//        List<TitleValueDataVO> itemTargetThreeScore = getItemTargetThreeScore(optionList);
        // 算出 当前科室 三级指标的排名
        List<SingleOfficeData> currentQuestionScore = getCurrentQuestionScore(optionList, officeId);
        // 获取当前科室的题目
//        currentTargetThreeScore.stream().map(TitleValueDataVO::getId)


        SingleOfficeSatisfyData data = new SingleOfficeSatisfyData();
        data.setOfficeSatisfyValue(info.getSatisfyValue());
        data.setAnswerQuantity(info.getAnswerQuantity());
        data.setLevelValue(info.getLevel());
//        data.setTitleList();
        data.setQuestionList(currentQuestionScore);



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
        long answerQuantity = optionList.stream().filter(v->v.getOfficeId().equals(officeId)).map(ReportAnswerOption::getAnswerId).distinct().count();
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

    private List<SingleOfficeData> getCurrentQuestionScore(List<ReportAnswerOption> optionList, Integer officeId) {
        List<SingleOfficeData> result = new ArrayList<>();
        List<ReportAnswerOption> currentOfficeQuestionList = optionList.stream().filter(v -> v.getOfficeId().equals(officeId)).collect(Collectors.toList());

        // 按 题目 划分
        Map<Integer, List<ReportAnswerOption>> questionGroup = currentOfficeQuestionList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));
        for (Integer questionId : questionGroup.keySet()) {
            List<ReportAnswerOption> options = questionGroup.get(questionId);
            // 结果
            SingleOfficeData data = new SingleOfficeData();
            data.setQuestionId(questionId);
//            data.setQuestionName();



//            data.setHospitalSatisfyValue();
//            data.setQuestionLevel();


            // 计算题目满意度
            int number = 0;
            int score = 0;
            // 累计
            for (ReportAnswerOption row : options) {
                number += row.getQuantity();
                score += row.getQuantity() * row.getScore();
            }

            // 记录题目答题数量
            data.setValue1(0.0D);
            data.setValue2(0.0D);
            data.setValue3(0.0D);
            data.setValue4(0.0D);
            data.setValue5(0.0D);
            options.sort(Comparator.comparing(ReportAnswerOption::getScore).reversed());
            for (int i = 1; i <= options.size(); i++) {
                ReportAnswerOption reportAnswerOption = options.get(i - 1);
                switch (i) {
                    case 1:
                        double val1 = (double) reportAnswerOption.getQuantity() / (double) number;
                        data.setValue1(new BigDecimal(val1 * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        break;
                    case 2:
                        double val2 = (double) reportAnswerOption.getQuantity() / (double) number;
                        data.setValue2(new BigDecimal(val2 * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        break;
                    case 3:
                        double val3 = (double) reportAnswerOption.getQuantity() / (double) number;
                        data.setValue3(new BigDecimal(val3 * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        break;
                    case 4:
                        double val4 = (double) reportAnswerOption.getQuantity() / (double) number;
                        data.setValue4(new BigDecimal(val4 * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        break;
                    case 5:
                        double val5 = (double) reportAnswerOption.getQuantity() / (double) number;
                        data.setValue5(new BigDecimal(val5 * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        break;
                    default:
                        break;
                }
            }
            data.setCountValue(data.getValue1() + data.getValue2());
            // 满意度
            if (NumberUtil.nonZero(number)) {
                double val = new BigDecimal((double)score / (double)number).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                data.setQuestionSatisfyValue(val);
            }
            result.add(data);
        }
        // 设置题目标题
        List<Integer> questionIds = result.stream().map(SingleOfficeData::getQuestionId).collect(Collectors.toList());
        Wrapper<Question> questionWrapper = new EntityWrapper<Question>()
                .setSqlSelect(Question.ID, Question.TITLE)
                .in(Question.ID, questionIds);
        List<Question> tempQuestionList = questionService.selectList(questionWrapper);
        Map<Integer, String> questionMap = tempQuestionList.stream().collect(Collectors.toMap(Question::getId, Question::getTitle));
        result.forEach(row -> {
            String title = questionMap.get(row.getQuestionId());
            if (Objects.nonNull(title)) {
                row.setQuestionName(title);
            }
        });

//        // 当前科室满意度
//        result.sort(Comparator.comparing(TitleValueDataVO::getValue).reversed());
//        // 排序次数过滤
//        int rank = 0;
//        double rankValue = 0;
//        int tempRank = 0;
//        for (TitleValueDataVO t : result) {
//            if (rank == 0) {
//                rank++;
//                rankValue = t.getValue();
//                t.setRankValue(rank);
//            } else if (rankValue != t.getValue()) {
//                rank++;
//                rankValue = t.getValue();
//                rank += tempRank;
//                tempRank = 0;
//            } else {
//                tempRank++;
//            }
//            t.setRankValue(rank);
//        }
        return result;
    }

    private List<TitleValueDataVO> getItemQuestionScore(List<ReportAnswerOption> optionList) {
        List<TitleValueDataVO> result = new ArrayList<>();
        // 按三级指标划分
        Map<Integer, List<ReportAnswerOption>> questionGroup = optionList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));
        for (List<ReportAnswerOption> questionList : questionGroup.values()) {
//            ReportAnswerOption targetThreeRow = questionList.get(0);
//            int targetNumber = 0;
//            double targetScore = 0;
//            // 计算每道题的得分
//            Map<Integer, List<ReportAnswerOption>> questionGroup = targetThree.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));
//            for (List<ReportAnswerOption> option : questionGroup.values()) {
//                int number = 0;
//                int score = 0;
//                // 累计
//                for (ReportAnswerOption row : option) {
//                    number += row.getQuantity();
//                    score += row.getQuantity() * row.getScore();
//                }
//                option.sort(Comparator.comparing(ReportAnswerOption::getScore).reversed());
//
//                // 满意度
//                if (NumberUtil.nonZero(number)) {
//                    double val = (double)score / (double)number;
//                    targetScore += new BigDecimal(val).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//                    targetNumber++;
//                }
//            }
//            // 三级指标得分
//            double score = new BigDecimal(targetScore / (double) targetNumber).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//            // 保存数据
//            TitleValueDataVO data = new TitleValueDataVO();
//            data.setId(targetThreeRow.getTargetThree());
//            data.setValue(score);
//            result.add(data);
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
