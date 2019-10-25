package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.*;
import com.sunchs.lyt.db.business.service.impl.*;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.framework.util.ObjectUtil;
import com.sunchs.lyt.framework.util.PagingUtil;
import com.sunchs.lyt.report.bean.*;
import com.sunchs.lyt.report.exception.ReportException;
import com.sunchs.lyt.report.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService implements IReportService {

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private AnswerServiceImpl answerService;

    @Autowired
    private ReportAnswerServiceImpl reportAnswerService;

    @Autowired
    private ReportAnswerOptionServiceImpl reportAnswerOptionService;

    @Autowired
    private QuestionOptionServiceImpl questionOptionService;

    @Override
    public PagingList<ItemTotalData> getItemTotalList(ItemTotalParam param) {
        List<ItemTotalData> list = new ArrayList<>();
        Page<Item> itemList = getItemList(param.getPageNow(), param.getPageSize());
        itemList.getRecords().forEach(q -> {
            ItemTotalData data = ObjectUtil.copy(q, ItemTotalData.class);
            // 合格数里
            data.setPassQuantity(getPassQuantity(q.getId()));
            // 待审核数里
            data.setWaitQuantity(getWaitQuantity(q.getId()));

            list.add(data);
        });
        return PagingUtil.getData(list, itemList.getTotal(), itemList.getCurrent(), itemList.getSize());
    }

    @Override
    public List<AnswerQuestionData> getAnswerQuestionList(AnswerQuestionParam param) {
        if (NumberUtil.isZero(param.getItemId())) {
            throw new ReportException("项目ID，不能为空");
        }
        List<AnswerQuestionData> result = new ArrayList<>();
//        // 答卷ID
//        List<Integer> answerIds = new ArrayList<>();
//
//        Wrapper<ReportAnswer> reportAnswerWrapper = new EntityWrapper<>();
//        reportAnswerWrapper.setSqlSelect(ReportAnswer.ID);
//        reportAnswerWrapper.eq(ReportAnswer.ITEM_ID, param.getItemId());
//        List<ReportAnswer> reportAnswers = reportAnswerService.selectList(reportAnswerWrapper);
//        reportAnswers.forEach(reportAnswer -> answerIds.add(reportAnswer.getId()));
//        if (answerIds.size() == 0) {
//            return result;
//        }
        // 题目集合
        Map<Integer, ReportAnswerOption> questionMap = new HashMap<>();

        Wrapper<ReportAnswerOption> reportAnswerOptionWrapper = new EntityWrapper<>();
//        reportAnswerOptionWrapper.in(ReportAnswerOption.ANSWER_ID, answerIds);
        reportAnswerOptionWrapper.addFilter("answer_id IN (SELECT id FROM report_answer WHERE item_id = "+param.getItemId()+")");
        List<ReportAnswerOption> answerOptionList = reportAnswerOptionService.selectList(reportAnswerOptionWrapper);
        answerOptionList.forEach(a -> {
            if ( ! questionMap.containsKey(a.getQuestionId())) {
                questionMap.put(a.getQuestionId(), a);
            }
        });
        // 答卷总条数
        int allQty = answerOptionList.size();
        System.out.println("答卷总条数"+allQty);
        System.out.println("题目数量："+questionMap.size());

        if (questionMap.size() == 0) {
            return result;
        }
//        Set<Integer> qIds = questionMap.keySet();
//
//        Wrapper<QuestionOption> questionOptionWrapper = new EntityWrapper<>();
//        questionOptionWrapper.setSqlSelect(QuestionOption.ID, QuestionOption.QUESTION_ID, QuestionOption.TITLE);
//        questionOptionWrapper.in(QuestionOption.QUESTION_ID, qIds);
//        questionOptionWrapper.orderBy(QuestionOption.SORT, true);
//        List<QuestionOption> oOptionList = questionOptionService.selectList(questionOptionWrapper);

        for (ReportAnswerOption row : questionMap.values()) {
            AnswerQuestionData data = new AnswerQuestionData();
            data.setAnswerId(row.getAnswerId());
            data.setQuestionId(row.getQuestionId());
            data.setQuestionName(row.getQuestionName());

            List<ReportAnswerOption> oqList = answerOptionList.stream().filter(q -> q.getQuestionId().equals(row.getQuestionId())).collect(Collectors.toList());
            // 题目总条数
            int questionQty = oqList.size();
            data.setQuestionQuantity(questionQty);
            data.setQuestionRateValue(questionQty / allQty);

//            List<AnswerQuestionOptionData> answerQuestionOptionList = new ArrayList<>();
//            oOptionList.forEach(oo->{
//                if (oo.getQuestionId().equals(row.getQuestionId())) {
//                    AnswerQuestionOptionData answerQuestionOptionData = new AnswerQuestionOptionData();
//                    answerQuestionOptionData.setOptionId(oo.getId());
//                    answerQuestionOptionData.setOptionName(oo.getTitle());
//
//                    List<ReportAnswerOption> ooList = answerOptionList.stream().filter(q ->
//                            q.getQuestionId().equals(row.getQuestionId()) && q.getOptionId().equals(row.getOptionId())).collect(Collectors.toList());
//                    // 选项总数量
//                    int optionQty = ooList.size();
//                    answerQuestionOptionData.setOptionQuantity(optionQty);
//                    answerQuestionOptionData.setOptionRateValue(NumberUtil.format(optionQty / questionQty));
//                    answerQuestionOptionList.add(answerQuestionOptionData);
//                }
//            });
//            data.setOptionList(answerQuestionOptionList);
            result.add(data);
        }

        return result;
    }

    /**
     * 获取项目列表
     */
    private Page<Item> getItemList(int pageNow, int pageSize) {
        Wrapper<Item> wrapper = new EntityWrapper<>();
        wrapper.ne(Item.STATUS, 0);
        return itemService.selectPage(new Page<>(pageNow, pageSize), wrapper);
    }

    /**
     * 获取合格数里
     */
    private int getPassQuantity(int itemId) {
        Wrapper<ReportAnswer> wrapper = new EntityWrapper<>();
        wrapper.eq(ReportAnswer.ITEM_ID, itemId);
        return reportAnswerService.selectCount(wrapper);
    }

    /**
     * 获取未审核数里
     */
    private int getWaitQuantity(int itemId) {
        Wrapper<Answer> wrapper = new EntityWrapper<>();
        wrapper.eq(Answer.ITEM_ID, itemId);
        return answerService.selectCount(wrapper);
    }
}