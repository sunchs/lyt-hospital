package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.*;
import com.sunchs.lyt.db.business.service.impl.*;
import com.sunchs.lyt.framework.constants.CacheKeys;
import com.sunchs.lyt.framework.constants.DateTimes;
import com.sunchs.lyt.framework.util.JsonUtil;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.framework.util.ObjectUtil;
import com.sunchs.lyt.framework.util.RedisUtil;
import com.sunchs.lyt.report.exception.ReportException;
import com.sunchs.lyt.report.service.IReportFactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReportFactoryService implements IReportFactoryService {

    @Autowired
    private ItemOfficeServiceImpl itemOfficeService;

    @Autowired
    private ReportAnswerOptionServiceImpl reportAnswerOptionService;

    @Autowired
    private ReportAnswerServiceImpl reportAnswerService;

    @Autowired
    private ReportAnswerQuantityServiceImpl reportAnswerQuantityService;

    @Autowired
    private ReportAnswerSatisfyServiceImpl reportAnswerSatisfyService;

    @Autowired
    private QuestionOptionServiceImpl questionOptionService;

    @Autowired
    private QuestionServiceImpl questionService;

    @Override
    public void makeAnswerQuantity() {
        if (RedisUtil.exists(CacheKeys.MAKE_ANSWER_QUANTITY)) {
            throw new ReportException("数据正在生成中，请稍后再试!");
        }
        try {
            // 生成锁
            RedisUtil.setValue(CacheKeys.MAKE_ANSWER_QUANTITY, "true", DateTimes.MONTH * 3);

            Wrapper<ItemOffice> itemOfficeWrapper = new EntityWrapper<>();
            itemOfficeWrapper.andNew("item_id IN (SELECT id FROM item WHERE status=1)");
            List<ItemOffice> itemOfficeList = itemOfficeService.selectList(itemOfficeWrapper);
            itemOfficeList.forEach(itemOffice -> {
                // 通过项目对于的答卷ID
                Wrapper<ReportAnswer> reportAnswerWrapper = new EntityWrapper<>();
                reportAnswerWrapper.setSqlSelect(ReportAnswer.ID);
                reportAnswerWrapper.eq(ReportAnswer.ITEM_ID, itemOffice.getItemId());
                reportAnswerWrapper.eq(ReportAnswer.OFFICE_ID, itemOffice.getOfficeId());
                reportAnswerWrapper.eq(ReportAnswer.QUESTIONNAIRE_ID, itemOffice.getQuestionnaireId());
                List<Object> answerIdList = reportAnswerService.selectObjs(reportAnswerWrapper);
                List<Long> answerIds = new ArrayList<>();
                answerIdList.forEach(id->answerIds.add((long)id));

                // 删除已统计的历史记录
                Wrapper<ReportAnswerQuantity> reportAnswerQuantityWrapper = new EntityWrapper<>();
                reportAnswerQuantityWrapper.eq(ReportAnswerQuantity.HOSPITAL_ID, itemOffice.getHospitalId());
                reportAnswerQuantityWrapper.eq(ReportAnswerQuantity.ITEM_ID, itemOffice.getItemId());
                reportAnswerQuantityWrapper.eq(ReportAnswerQuantity.OFFICE_ID, itemOffice.getOfficeId());
                reportAnswerQuantityWrapper.eq(ReportAnswerQuantity.QUESTIONNAIRE_ID, itemOffice.getQuestionnaireId());
                reportAnswerQuantityService.delete(reportAnswerQuantityWrapper);

                // 生产新统计数据
                if (answerIdList.size() > 0) {
                    List<ReportAnswerQuantity> list = reportAnswerOptionService.getReportAnswerData(answerIds);
                    list.forEach(r -> {
                        // 合并数据
                        r.setHospitalId(itemOffice.getHospitalId());
                        r.setItemId(itemOffice.getItemId());
                        r.setOfficeId(itemOffice.getOfficeId());
                        r.setQuestionnaireId(itemOffice.getQuestionnaireId());

                        // 插入数据
                        ReportAnswerQuantity qty = ObjectUtil.copy(r, ReportAnswerQuantity.class);
                        qty.setScore(getOptionScore(qty.getOptionId()));
                        // 指标
                        Question question = getQuestion(qty.getQuestionId());
                        if (Objects.nonNull(question)) {
                            qty.setTargetOne(question.getTargetOne());
                            qty.setTargetTwo(question.getTargetTwo());
                            qty.setTargetThree(question.getTargetThree());
                        }
                        reportAnswerQuantityService.insert(qty);
                    });
                }
            });
        } catch (Exception e) {
            System.out.println("生成答题数量，异常："+e.getMessage());
            e.printStackTrace();
        } finally {
            // 清理生成锁
            RedisUtil.remove(CacheKeys.MAKE_ANSWER_QUANTITY);
        }
    }

    @Override
    public void makeAnswerSatisfy() {
        if (RedisUtil.exists(CacheKeys.MAKE_ANSWER_SATISFY)) {
            throw new ReportException("数据正在生成中，请稍后再试!");
        }
        try {
            // 生成锁
            RedisUtil.setValue(CacheKeys.MAKE_ANSWER_SATISFY, "true", DateTimes.MONTH * 3);

            Wrapper<ItemOffice> itemOfficeWrapper = new EntityWrapper<>();
            itemOfficeWrapper.andNew("item_id IN (SELECT id FROM item WHERE status=1)");
            List<ItemOffice> itemOfficeList = itemOfficeService.selectList(itemOfficeWrapper);
            itemOfficeList.forEach(itemOffice -> {
                // 删除已统计的历史记录
                Wrapper<ReportAnswerSatisfy> reportAnswerSatisfyWrapper = new EntityWrapper<>();
                reportAnswerSatisfyWrapper.eq(ReportAnswerSatisfy.HOSPITAL_ID, itemOffice.getHospitalId());
                reportAnswerSatisfyWrapper.eq(ReportAnswerSatisfy.ITEM_ID, itemOffice.getItemId());
                reportAnswerSatisfyWrapper.eq(ReportAnswerSatisfy.OFFICE_ID, itemOffice.getOfficeId());
                reportAnswerSatisfyWrapper.eq(ReportAnswerSatisfy.QUESTIONNAIRE_ID, itemOffice.getQuestionnaireId());
                reportAnswerSatisfyService.delete(reportAnswerSatisfyWrapper);

                // 计算满意度
                Wrapper<ReportAnswerQuantity> quantityWrapper = new EntityWrapper<>();
                quantityWrapper.eq(ReportAnswerQuantity.HOSPITAL_ID, itemOffice.getHospitalId());
                quantityWrapper.eq(ReportAnswerQuantity.ITEM_ID, itemOffice.getItemId());
                quantityWrapper.eq(ReportAnswerQuantity.OFFICE_ID, itemOffice.getOfficeId());
                quantityWrapper.eq(ReportAnswerQuantity.QUESTIONNAIRE_ID, itemOffice.getQuestionnaireId());
                List<ReportAnswerQuantity> quantityList = reportAnswerQuantityService.selectList(quantityWrapper);
                Map<Integer, List<ReportAnswerQuantity>> mapGroup = quantityList.stream().collect(Collectors.groupingBy(ReportAnswerQuantity::getQuestionId));
                for (List<ReportAnswerQuantity> group : mapGroup.values()) {
                    ReportAnswerQuantity old = group.get(0);
                    int number = 0;
                    int score = 0;
                    ReportAnswerSatisfy data = new ReportAnswerSatisfy();
                    data.setHospitalId(old.getHospitalId());
                    data.setItemId(old.getItemId());
                    data.setOfficeId(old.getOfficeId());
                    data.setQuestionnaireId(old.getQuestionnaireId());
                    // 累计
                    for (ReportAnswerQuantity row : group) {
                        number += row.getQuantity();
                        score += row.getQuantity() * row.getScore();
                        data.setQuestionId(row.getQuestionId());
                        data.setQuestionName(row.getQuestionName());
                    }
                    // 指标
                    data.setTargetOne(old.getTargetOne());
                    data.setTargetTwo(old.getTargetTwo());
                    data.setTargetThree(old.getTargetThree());
//                    Question question = getQuestion(data.getQuestionId());
//                    if (Objects.nonNull(question)) {
//                        data.setTargetOne(question.getTargetOne());
//                        data.setTargetTwo(question.getTargetTwo());
//                        data.setTargetThree(question.getTargetThree());
//                    }
                    // 满意度
                    if (NumberUtil.nonZero(number)) {
                        int val = (int)(score / number * 100);
                        data.setScore(val);
                    } else {
                        data.setScore(0);
                    }
                    // 插入新数据
                    reportAnswerSatisfyService.insert(data);
                }
            });
        } catch (Exception e) {
            System.out.println("生成答题满意度，异常："+e.getMessage());
            e.printStackTrace();
        } finally {
            // 清理生成锁
            RedisUtil.remove(CacheKeys.MAKE_ANSWER_SATISFY);
        }
    }

    private Integer getOptionScore(Integer optionId) {
        QuestionOption data = questionOptionService.selectById(optionId);
        return Objects.nonNull(data) ? data.getScore() : 0;
    }

    private Question getQuestion(Integer questionId) {
        return questionService.selectById(questionId);
    }
}
