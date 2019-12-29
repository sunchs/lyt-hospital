package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.*;
import com.sunchs.lyt.db.business.service.impl.*;
import com.sunchs.lyt.framework.bean.IdTitleData;
import com.sunchs.lyt.framework.bean.TitleValueData;
import com.sunchs.lyt.framework.util.FormatUtil;
import com.sunchs.lyt.report.bean.*;
import com.sunchs.lyt.report.service.IReportCompareService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportCompareService implements IReportCompareService {

    @Autowired
    private ReportAnswerSatisfyServiceImpl reportAnswerSatisfyService;

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private ReportAnswerOptionServiceImpl reportAnswerOptionService;

    @Autowired
    private SettingItemWeightServiceImpl settingItemWeightService;

    @Autowired
    private QuestionTargetServiceImpl questionTargetService;

    @Autowired
    private ItemOfficeServiceImpl itemOfficeService;

    @Override
    public List<Item> getItemListByOfficeType(Integer officeType) {
        // 查询项目ID
        Wrapper<ReportAnswerSatisfy> satisfyWrapper = new EntityWrapper<ReportAnswerSatisfy>()
                .setSqlSelect(ReportAnswerSatisfy.ITEM_ID + " as itemId")
                .eq(ReportAnswerSatisfy.TARGET_ONE, officeType)
                .groupBy(ReportAnswerSatisfy.ITEM_ID);
        List<ReportAnswerSatisfy> satisfyList = reportAnswerSatisfyService.selectList(satisfyWrapper);
        List<Integer> itemIds = satisfyList.stream().map(ReportAnswerSatisfy::getItemId).collect(Collectors.toList());
        // 查询项目
        Wrapper<Item> itemWrapper = new EntityWrapper<Item>()
                .in(Item.ID, itemIds);
        return itemService.selectList(itemWrapper);
    }

    @Override
    public List<QuestionTarget> getItemTargetThreeByOfficeType(Integer itemId, Integer officeType) {
        Wrapper<ReportAnswerOption> optionWrapper = new EntityWrapper<ReportAnswerOption>()
                .setSqlSelect(ReportAnswerOption.TARGET_THREE + " as targetThree")
                .eq(ReportAnswerOption.ITEM_ID, itemId)
                .eq(ReportAnswerOption.OFFICE_TYPE_ID, officeType)
                .in(ReportAnswerOption.OPTION_TYPE, Arrays.asList(1, 4))
                .groupBy(ReportAnswerOption.TARGET_THREE);
        List<ReportAnswerOption> targetThreeList = reportAnswerOptionService.selectList(optionWrapper);
        if (CollectionUtils.isEmpty(targetThreeList)) {
            return new ArrayList<>();
        }
        List<Integer> targetIds = targetThreeList.stream().map(ReportAnswerOption::getTargetThree).collect(Collectors.toList());
        Wrapper<QuestionTarget> targetWrapper = new EntityWrapper<QuestionTarget>()
                .in(QuestionTarget.ID, targetIds);
        return questionTargetService.selectList(targetWrapper);
    }

    @Override
    public ItemCompareData getItemCompareInfo(ItemCompareParam param) {
        ItemCompareData data = new ItemCompareData();
        if (Objects.isNull(param.getValueList()) || param.getValueList().size() == 0) {
            return data;
        }
        List<ItemCompareBean> valueList = param.getValueList();
        List<Integer> itemIds = valueList.stream().map(ItemCompareBean::getItemId).collect(Collectors.toList());
        Map<Integer, String> itemNameMap = getItemNameByIds(itemIds);

        List<SatisfyData> satisfyDataList = new ArrayList<>();
        List<IdTitleData> colList = new ArrayList<>();
        List<IdTitleData> rowList = new ArrayList<>();
        List<ItemCompareValue> vList = new ArrayList<>();
        // 设置列值
        valueList.forEach(item->{
            // 设置满意度列和值
            Double satisfyValue = getItemAllSatisfy(item.getItemId(), item.getOfficeType(), item.getStartTime(), item.getEndTime());
            SatisfyData satisfyData = new SatisfyData();
            satisfyData.setId(item.getItemId());
            satisfyData.setName(itemNameMap.get(item.getItemId()));
            satisfyData.setValue(satisfyValue);
            satisfyDataList.add(satisfyData);

            // 设置题目列
            IdTitleData questionColData = new IdTitleData();
            questionColData.setId(item.getItemId());
            questionColData.setTitle(itemNameMap.get(item.getItemId()));
            colList.add(questionColData);
            // 列索引
            item.setColIndex(valueList.indexOf(item));

            List<ReportAnswerOption> tempOptionList = getItemAnswerOption(item.getItemId(), item.getOfficeType(), item.getStartTime(), item.getEndTime());
            item.setTempOptionList(tempOptionList);
        });
        data.setAllSatisfyList(satisfyDataList);
        data.setColList(colList);

        // 对比数据
        valueList.forEach(item->{
            // 题目分组
            List<ReportAnswerOption> tempOptionList = item.getTempOptionList();
            Map<Integer, List<ReportAnswerOption>> questionMap = tempOptionList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));
            for (Integer questionId : questionMap.keySet()) {
                List<ReportAnswerOption> optionList = questionMap.get(questionId);
                // 插入新行
                if (isInRowList(questionId, rowList) == false) {
                    IdTitleData tData = new IdTitleData();
                    tData.setId(optionList.get(0).getQuestionId());
                    tData.setTitle(optionList.get(0).getQuestionName());
                    rowList.add(tData);
                }
                // 计算满意度
                double value = 0;
                int number = 0;
                for (ReportAnswerOption option : optionList) {
                    value += option.getScore().doubleValue() * option.getQuantity().doubleValue();
                    number += option.getQuantity().intValue();
                }
                if (number > 0) {
                    ItemCompareValue vObj = new ItemCompareValue();
                    vObj.setRowId(questionId);
                    vObj.setColId(item.getItemId());
                    vObj.setColIndex(item.getColIndex());
                    vObj.setValue(new BigDecimal(value / (double) number).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    vList.add(vObj);
                }
            }
        });
        data.setRowList(rowList);
        data.setValueList(vList);
        return data;
    }

    @Override
    public List<TitleValueData> getItemTargetCompareInfo(ItemCompareParam param) {
        List<TitleValueData> result = new ArrayList<>();
        Date sTime = FormatUtil.dateTime(param.getStartTime());
        Date eTime = FormatUtil.dateTime(param.getEndTime());
        Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                .setSqlSelect(
                        "office_id AS officeId,question_id AS questionId,option_id AS optionId,score,COUNT(1) quantity"
                )
                .eq(ReportAnswerOption.ITEM_ID, param.getItemId())
                .eq(ReportAnswerOption.OFFICE_TYPE_ID, param.getOfficeType())
                .ge(ReportAnswerOption.ENDTIME, sTime)
                .le(ReportAnswerOption.ENDTIME, eTime)
                .in(ReportAnswerOption.OPTION_TYPE, Arrays.asList(1, 4))
                .eq(ReportAnswerOption.TARGET_THREE, param.getTargetThree())
                .groupBy(ReportAnswerOption.OFFICE_ID)
                .groupBy(ReportAnswerOption.QUESTION_ID)
                .groupBy(ReportAnswerOption.OPTION_ID);
        List<ReportAnswerOption> officeOptionList = reportAnswerOptionService.selectList(wrapper);
//        if (CollectionUtils.isEmpty(officeOptionList)) {
//            return result;
//        }
        Map<Integer, List<ReportAnswerOption>> officeMap = officeOptionList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getOfficeId));
        // 查询所有科室
        Wrapper<ItemOffice> itemOfficeWrapper = new EntityWrapper<ItemOffice>()
                .eq(ItemOffice.ITEM_ID, param.getItemId())
                .eq(ItemOffice.OFFICE_TYPE_ID, param.getOfficeType());
        List<ItemOffice> itemOfficeList = itemOfficeService.selectList(itemOfficeWrapper);
        itemOfficeList.forEach(office->{
            TitleValueData data = new TitleValueData();
            data.setId(office.getOfficeId());
            data.setTitle(office.getTitle());
            List<ReportAnswerOption> questionOptionList = officeMap.get(office.getOfficeId());
            if (CollectionUtils.isEmpty(questionOptionList)) {
                data.setValue(-1d);
                result.add(data);
            } else {
                // 计算满意度
                Map<Integer, List<ReportAnswerOption>> questionMap = questionOptionList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));
                for (Integer questionId : questionMap.keySet()) {
                    List<ReportAnswerOption> optionList = questionMap.get(questionId);
                    // 计算满意度
                    double value = 0;
                    int number = 0;
                    for (ReportAnswerOption option : optionList) {
                        value += option.getScore().doubleValue() * option.getQuantity().doubleValue();
                        number += option.getQuantity().intValue();
                    }
                    if (number > 0) {
                        double val = new BigDecimal(value / (double) number).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        data.setValue(val);
                        result.add(data);
                    }
                }
            }
        });
        return result;
    }

    private Double getItemAllSatisfy(Integer itemId, Integer officeType, String startTime, String endTime) {
        double allScore = 0;
        Wrapper<SettingItemWeight> weightWrapper = new EntityWrapper<SettingItemWeight>()
                .eq(SettingItemWeight.ITEM_ID, itemId)
                .eq(SettingItemWeight.OFFICE_TYPE, officeType);
        List<SettingItemWeight> weightList = settingItemWeightService.selectList(weightWrapper);
        if (Objects.isNull(weightList) || weightList.size() == 0) {
            return allScore;
        }
        for (SettingItemWeight weight : weightList) {
            List<String> targetThreeStringList = Arrays.asList(weight.getTargetThree().split(","));
            List<Integer> targetThreeIds = targetThreeStringList.stream().map(v -> Integer.parseInt(v)).collect(Collectors.toList());
            double score = getSatisfyByTargetIds(itemId, officeType, targetThreeIds, startTime, endTime);
            allScore += weight.getWeight().doubleValue() * score;
        }
        return new BigDecimal(allScore).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private double getSatisfyByTargetIds(Integer itemId, Integer officeType, List<Integer> targetThreeIds, String startTime, String endTime) {
        Date sTime = FormatUtil.dateTime(startTime);
        Date eTime = FormatUtil.dateTime(endTime);
        double score = 0;
        Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                .setSqlSelect("question_id AS questionId,option_id AS optionId,score,COUNT(1) quantity")
                .eq(ReportAnswerOption.ITEM_ID, itemId)
                .eq(ReportAnswerOption.OFFICE_TYPE_ID, officeType)
                .ge(ReportAnswerOption.ENDTIME, sTime)
                .le(ReportAnswerOption.ENDTIME, eTime)
                .in(ReportAnswerOption.OPTION_TYPE, Arrays.asList(1, 4))
                .in(ReportAnswerOption.TARGET_THREE, targetThreeIds)
                .ne(ReportAnswerOption.SCORE, 0)
                .groupBy(ReportAnswerOption.QUESTION_ID)
                .groupBy(ReportAnswerOption.OPTION_ID);
        List<ReportAnswerOption> list = reportAnswerOptionService.selectList(wrapper);
        if (Objects.isNull(list) || list.size() == 0) {
            return score;
        }
        // 计算满意度
        Map<Integer, List<ReportAnswerOption>> questionMap = list.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));
        for (Integer questionId : questionMap.keySet()) {
            List<ReportAnswerOption> optionList = questionMap.get(questionId);
            // 计算满意度
            double value = 0;
            int number = 0;
            for (ReportAnswerOption option : optionList) {
                value += option.getScore().doubleValue() * option.getQuantity().doubleValue();
                number += option.getQuantity().intValue();
            }
            if (number > 0) {
                score += new BigDecimal(value / (double) number).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }
        return score / (double) questionMap.size();
    }

    private boolean isInRowList(Integer questionId, List<IdTitleData> rowList) {
        if (rowList.size() == 0) {
            return false;
        }
        List<IdTitleData> list = rowList.stream().filter(v -> v.getId().equals(questionId)).collect(Collectors.toList());
        if (Objects.nonNull(list) && list.size() > 0) {
            return true;
        }
        return false;
    }

    private List<ReportAnswerOption> getItemAnswerOption(Integer itemId, Integer optionType, String startTime, String endTime) {
        Date sTime = FormatUtil.dateTime(startTime);
        Date eTime = FormatUtil.dateTime(endTime);
        Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                .setSqlSelect(
                        "question_id AS questionId,question_name AS questionName,option_id AS optionId,score,COUNT(1) quantity"
//                                "option_name AS optionName,target_one AS targetOne,target_two AS targetTwo,target_three AS targetThree, " +
                )
                .eq(ReportAnswerOption.ITEM_ID, itemId)
                .eq(ReportAnswerOption.OFFICE_TYPE_ID, optionType)
                .ge(ReportAnswerOption.ENDTIME, sTime)
                .le(ReportAnswerOption.ENDTIME, eTime)
                .in(ReportAnswerOption.OPTION_TYPE, Arrays.asList(1, 4))
                .groupBy(ReportAnswerOption.QUESTION_ID)
                .groupBy(ReportAnswerOption.OPTION_ID);
        return reportAnswerOptionService.selectList(wrapper);
    }

    /**
     * 根据 项目ID集合 获取项目名称
     */
    private Map<Integer, String> getItemNameByIds(List<Integer> itemIds) {
        Wrapper<Item> wrapper = new EntityWrapper<Item>()
                .setSqlSelect(Item.ID, Item.TITLE)
                .in(Item.ID, itemIds);
        List<Item> itemList = itemService.selectList(wrapper);
        return itemList.stream().collect(Collectors.toMap(Item::getId, Item::getTitle));
    }
}
