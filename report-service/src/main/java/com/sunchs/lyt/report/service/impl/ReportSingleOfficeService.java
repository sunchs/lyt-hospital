package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.ReportAnswerOption;
import com.sunchs.lyt.db.business.entity.ReportItemScore;
import com.sunchs.lyt.db.business.service.impl.ReportAnswerOptionServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ReportItemScoreServiceImpl;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.report.bean.SingleOfficeSatisfyData;
import com.sunchs.lyt.report.service.IReportSingleOfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportSingleOfficeService implements IReportSingleOfficeService {

    @Autowired
    private ReportAnswerOptionServiceImpl reportAnswerOptionService;

    @Autowired
    private ReportItemScoreServiceImpl reportItemScoreService;


    @Override
    public SingleOfficeSatisfyData getItemSingleOfficeSatisfy(Integer itemId, Integer officeType, Integer officeId) {
        return null;
    }

    @Override
    public void setItemOfficeRanking(Integer itemId) {
        new Thread(()->{
            execMakeItemOfficeScore(itemId);
        }).start();
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

        // 按科室类型划分
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
    }
}
