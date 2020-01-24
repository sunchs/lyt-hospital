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
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    private SettingItemOfficeShowServiceImpl settingItemOfficeShowService;

    @Autowired
    private SettingItemTargetShowServiceImpl settingItemTargetShowService;

    @Autowired
    private QuestionTagServiceImpl questionTagService;

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
    public List<TitleData> getItemUseOffice(Integer itemId, Integer officeType) {
        List<TitleData> result = new ArrayList<>();
        // 获取默认选择项
        Wrapper<SettingItemOfficeShow> showWrapper = new EntityWrapper<>();
        showWrapper.setSqlSelect(SettingItemOfficeShow.OFFICE_ID+" as officeId");
        showWrapper.eq(SettingItemOfficeShow.ITEM_ID, itemId);
        showWrapper.eq(SettingItemOfficeShow.POSITION, 1);
        List<Integer> selectIds = (List<Integer>)(List)settingItemOfficeShowService.selectObjs(showWrapper);

        Wrapper<ItemOffice> wrapper = new EntityWrapper<ItemOffice>()
                .andNew("office_id IN(SELECT office_id FROM report_answer WHERE item_id="+itemId+")")
                .eq(ItemOffice.ITEM_ID, itemId)
                .eq(ItemOffice.OFFICE_TYPE_ID, officeType);
        List<ItemOffice> itemList = itemOfficeService.selectList(wrapper);
        itemList.forEach(v->{
            TitleData data = new TitleData();
            data.setId(v.getOfficeId());
            data.setTitle(v.getTitle());
            data.setSelected(selectIds.contains(v.getOfficeId()));
            result.add(data);
        });
        return result;
    }

    @Override
    public List<TitleData> getItemUseTarget(Integer itemId, Integer officeType) {
        List<TitleData> result = new ArrayList<>();
        // 获取默认选择项
        Wrapper<SettingItemTargetShow> showWrapper = new EntityWrapper<>();
        showWrapper.setSqlSelect(SettingItemTargetShow.TARGET_ID+" as targetId");
        showWrapper.eq(SettingItemTargetShow.ITEM_ID, itemId);
        showWrapper.eq(SettingItemTargetShow.POSITION, 1);
        List<Integer> selectIds = (List<Integer>)(List)settingItemTargetShowService.selectObjs(showWrapper);

        Wrapper<QuestionTarget> wrapper = new EntityWrapper<QuestionTarget>()
                .andNew("id IN(SELECT target_three FROM report_answer_quantity WHERE item_id="+itemId+" AND target_one="+officeType+")");
        List<QuestionTarget> targetList = questionTargetService.selectList(wrapper);
        targetList.forEach(v->{
            TitleData data = new TitleData();
            data.setId(v.getId());
            data.setTitle(v.getTitle());
            data.setSelected(selectIds.contains(v.getId()));
            result.add(data);
        });
        return result;
    }

    @Override
    public void saveSettingItemOffice(SettingParam param) {
        if (Objects.isNull(param.getOfficeList()) || param.getOfficeList().size() == 0) {
            throw new ReportException("最少需要一个科室");
        }
        Wrapper<SettingItemOfficeShow> wrapper = new EntityWrapper<>();
        wrapper.eq(SettingItemOfficeShow.ITEM_ID, param.getItemId());
        wrapper.eq(SettingItemOfficeShow.POSITION, param.getPosition());
        settingItemOfficeShowService.delete(wrapper);

        param.getOfficeList().forEach(id -> {
            SettingItemOfficeShow data = new SettingItemOfficeShow();
            data.setItemId(param.getItemId());
            data.setOfficeId(id);
            data.setPosition(param.getPosition());
            settingItemOfficeShowService.insert(data);
        });
    }

    @Override
    public void saveSettingItemTarget(SettingParam param) {
        if (Objects.isNull(param.getTargetList()) || param.getTargetList().size() == 0) {
            throw new ReportException("最少需要一个指标");
        }
        Wrapper<SettingItemTargetShow> wrapper = new EntityWrapper<>();
        wrapper.eq(SettingItemTargetShow.ITEM_ID, param.getItemId());
        wrapper.eq(SettingItemTargetShow.POSITION, param.getPosition());
        settingItemTargetShowService.delete(wrapper);

        param.getTargetList().forEach(id -> {
            SettingItemTargetShow data = new SettingItemTargetShow();
            data.setItemId(param.getItemId());
            data.setTargetId(id);
            data.setPosition(param.getPosition());
            settingItemTargetShowService.insert(data);
        });
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

    @Override
    public List<Map<String, Object>> getItemTagMenu(Integer itemId, Integer officeType) {
        List<Map<String, Object>> result = new ArrayList<>();
        Wrapper<QuestionTag> wrapper = new EntityWrapper<QuestionTag>()
                .eq(QuestionTag.PID, 1);
        List<QuestionTag> questionTagList = questionTagService.selectList(wrapper);
        questionTagList.forEach(questionTag -> {
            Map<String, Object> row = new HashMap<>();
            row.put("id", questionTag.getId());
            row.put("title", questionTag.getTitle());
            List<Map<String, Object>> tagChildrenData = getTagChildrenData(itemId, officeType, questionTag.getId());
            if (CollectionUtils.isNotEmpty(tagChildrenData)) {
                row.put("children", tagChildrenData);
            }
            result.add(row);
        });
        return result;
    }

    private List<Map<String, Object>> getTagChildrenData(Integer itemId, Integer officeType, Integer tagId) {
        List<Map<String, Object>> result = new ArrayList<>();
        Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                .setSqlSelect(
                        ReportAnswerOption.OPTION_ID + " as optionId",
                        ReportAnswerOption.OPTION_NAME + " as optionName"
                )
                .eq(ReportAnswerOption.ITEM_ID, itemId)
                .eq(ReportAnswerOption.OFFICE_TYPE_ID, officeType)
                .and(" question_id IN(SELECT question_id FROM question_tag_binding WHERE tag_id="+tagId+")")
                .groupBy(ReportAnswerOption.OPTION_ID);
        List<ReportAnswerOption> reportAnswerOptionList = reportAnswerOptionService.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(reportAnswerOptionList)) {
            reportAnswerOptionList.forEach(option->{
                Map<String, Object> row = new HashMap<>();
                row.put("id", option.getOptionId());
                row.put("title", option.getOptionName());
                result.add(row);
            });
        }
        return result;
    }
}