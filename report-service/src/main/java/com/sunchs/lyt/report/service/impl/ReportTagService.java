package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.QuestionTagBinding;
import com.sunchs.lyt.db.business.entity.ReportAnswerOption;
import com.sunchs.lyt.db.business.entity.ReportAnswerQuantity;
import com.sunchs.lyt.db.business.service.impl.QuestionTagBindingServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ReportAnswerQuantityServiceImpl;
import com.sunchs.lyt.framework.bean.TitleValueData;
import com.sunchs.lyt.report.bean.ItemCrowdParam;
import com.sunchs.lyt.report.bean.TotalSexData;
import com.sunchs.lyt.report.service.IReportTagService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportTagService implements IReportTagService {

    @Autowired
    private ReportAnswerQuantityServiceImpl reportAnswerQuantityService;

    @Autowired
    private QuestionTagBindingServiceImpl questionTagBindingService;

    @Override
    public List<TotalSexData> getItemQuantityByTag(int itemId, int tagId, int targetOne) {
        List<TotalSexData> list = new ArrayList<>();
        // 查询
        Wrapper<ReportAnswerQuantity> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("question_id AS questionId,question_name AS questionName,option_id AS optionId,option_name AS optionName,SUM(quantity) as quantity");
        wrapper.eq(ReportAnswerQuantity.ITEM_ID, itemId);
        wrapper.eq(ReportAnswerQuantity.TARGET_ONE, targetOne);
        wrapper.where(ReportAnswerQuantity.QUESTION_ID+" IN (SELECT question_id FROM question_tag_binding WHERE tag_id="+tagId+")");
        wrapper.groupBy(ReportAnswerQuantity.OPTION_ID);
        List<ReportAnswerQuantity> reportAnswerQuantities = reportAnswerQuantityService.selectList(wrapper);
        reportAnswerQuantities.forEach(row->{
            TotalSexData data = new TotalSexData();
            data.setQuestionId(row.getQuestionId());
            data.setQuestionName(row.getQuestionName());
            data.setOptionId(row.getOptionId());
            data.setOptionName(row.getOptionName());
            data.setQuantity(row.getQuantity());
            list.add(data);
        });
        // 计算占比率
        list.forEach(row -> {
            int qty = 0;
            List<TotalSexData> collect = list.stream().filter(r -> r.getQuestionId().equals(row.getQuestionId())).collect(Collectors.toList());
            for (TotalSexData r : collect) {
                qty += r.getQuantity();
            }
            double val = (double)row.getQuantity() / (double)qty;
            double rote = new BigDecimal(val * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            row.setRate(rote);
        });
        return list;
    }

    @Override
    public List<TotalSexData> getItemQuantityByTarget(int itemId, int targetId, int position) {
        List<TotalSexData> list = new ArrayList<>();
        // 查询
        Wrapper<ReportAnswerQuantity> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("question_id AS questionId,question_name AS questionName,option_id AS optionId,option_name AS optionName,SUM(quantity) as quantity");
        wrapper.eq(ReportAnswerQuantity.ITEM_ID, itemId);
        switch (position) {
            case 1:
                wrapper.eq(ReportAnswerQuantity.TARGET_ONE, targetId);
                break;
            case 2:
                wrapper.eq(ReportAnswerQuantity.TARGET_TWO, targetId);
                break;
            case 3:
                wrapper.eq(ReportAnswerQuantity.TARGET_THREE, targetId);
                break;
            default:
                break;
        }
        wrapper.groupBy(ReportAnswerQuantity.OPTION_ID);
        List<ReportAnswerQuantity> reportAnswerQuantities = reportAnswerQuantityService.selectList(wrapper);
        reportAnswerQuantities.forEach(row->{
            TotalSexData data = new TotalSexData();
            data.setQuestionId(row.getQuestionId());
            data.setQuestionName(row.getQuestionName());
            data.setOptionId(row.getOptionId());
            data.setOptionName(row.getOptionName());
            data.setQuantity(row.getQuantity());
            list.add(data);
        });
        // 计算占比率
        list.forEach(row -> {
            int qty = 0;
            List<TotalSexData> collect = list.stream().filter(r -> r.getQuestionId().equals(row.getQuestionId())).collect(Collectors.toList());
            for (TotalSexData r : collect) {
                qty += r.getQuantity();
            }
            double val = (double)row.getQuantity() / (double)qty;
            double rote = new BigDecimal(val * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            row.setRate(rote);
        });
        return list;
    }

//    /**
//     * 根据 选项ID集合 获取对应的题目ID集合（已分组好）
//     */
//    private Map<Integer, List<QuestionTagBinding>> getTagQuestionIds(List<Integer> tagIds) {
//        Wrapper<QuestionTagBinding> wrapper = new EntityWrapper<QuestionTagBinding>()
//                .setSqlSelect(
//                        QuestionTagBinding.QUESTION_ID + " as questionId",
//                        QuestionTagBinding.TAG_ID + " as tagId"
//                )
//                .in(QuestionTagBinding.TAG_ID, tagIds);
//        List<QuestionTagBinding> questionTagBindingList = questionTagBindingService.selectList(wrapper);
//        return questionTagBindingList.stream().collect(Collectors.groupingBy(QuestionTagBinding::getTagId));
//    }
//
//    private TitleValueData getItemTag(Integer tagId) {
//        Wrapper<QuestionTagBinding> wrapper = new EntityWrapper<QuestionTagBinding>()
//                .setSqlSelect(QuestionTagBinding.QUESTION_ID + " as questionId")
//                .eq(QuestionTagBinding.TAG_ID, tagId);
//        List<QuestionTagBinding> questionTagBindingList = questionTagBindingService.selectList(wrapper);
//        if (CollectionUtils.isNotEmpty(questionTagBindingList)) {
////            questionTagBindingList.stream()
//        }
////        questionTagBindingList
//
////        questionTagBindings.forEach();
////        Map<String, Object> stringObjectMap = questionTagBindingService.selectMap(wrapper);
//    }
}
