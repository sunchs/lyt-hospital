package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.*;
import com.sunchs.lyt.db.business.service.impl.*;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.bean.TitleData;
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

    @Autowired
    private ItemOfficeServiceImpl itemOfficeService;

    @Autowired
    private QuestionTargetServiceImpl questionTargetService;

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

        // 题目集合
        Map<Integer, List<ReportAnswerOption>> questionMap = new HashMap<>();
        Wrapper<ReportAnswerOption> reportAnswerOptionWrapper = new EntityWrapper<>();
        reportAnswerOptionWrapper.addFilter("answer_id IN (SELECT id FROM report_answer WHERE item_id = "+param.getItemId()+")");
        List<ReportAnswerOption> answerOptionList = reportAnswerOptionService.selectList(reportAnswerOptionWrapper);
        answerOptionList.forEach(answerOption -> {
            List<ReportAnswerOption> sonList = questionMap.get(answerOption.getQuestionId());
            if (Objects.isNull(sonList)) {
                sonList = new ArrayList<>();
            }
            sonList.add(answerOption);
            questionMap.put(answerOption.getQuestionId(), sonList);
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

        for (List<ReportAnswerOption> sonList : questionMap.values()) {
            AnswerQuestionData data = new AnswerQuestionData();
            data.setQuestionId(sonList.get(0).getQuestionId());
            data.setQuestionName(sonList.get(0).getQuestionName());

//            sonList.forEach(row->{
//                List<ReportAnswerOption> oqList = new ArrayList<>();
//                answerOptionList.forEach(q-> {
//                    if (q.getQuestionId().equals(row.getQuestionId())) {
//                        oqList.add(q);
//                    }
//                });
//            });

//            // 题目总条数
//            int questionQty = oqList.size();
//            System.out.println(questionQty);
            data.setQuestionQuantity(sonList.size());
            data.setQuestionRateValue(sonList.size() / allQty);

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

    @Override
    public List<TitleData> getItemUseOffice(Integer itemId) {
        List<TitleData> result = new ArrayList<>();
        Wrapper<ItemOffice> wrapper = new EntityWrapper<ItemOffice>()
                .andNew("office_id IN(SELECT office_id FROM report_answer WHERE item_id="+itemId+")")
                .eq(ItemOffice.ITEM_ID, itemId);
        List<ItemOffice> itemList = itemOfficeService.selectList(wrapper);
        itemList.forEach(v->{
            TitleData data = new TitleData();
            data.setId(v.getOfficeId());
            data.setTitle(v.getTitle());
            data.setSelected(false);
            result.add(data);
        });
        return result;
    }

    @Override
    public List<TitleData> getItemUseTarget(Integer itemId) {
        List<TitleData> result = new ArrayList<>();
        Wrapper<QuestionTarget> wrapper = new EntityWrapper<QuestionTarget>()
                .andNew("id IN(SELECT target_three FROM report_answer_quantity WHERE item_id="+itemId+")");
        List<QuestionTarget> targetList = questionTargetService.selectList(wrapper);
        targetList.forEach(v->{
            TitleData data = new TitleData();
            data.setId(v.getId());
            data.setTitle(v.getTitle());
            data.setSelected(false);
            result.add(data);
        });
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