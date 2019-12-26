package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.Item;
import com.sunchs.lyt.db.business.entity.ReportAnswerOption;
import com.sunchs.lyt.db.business.entity.ReportAnswerSatisfy;
import com.sunchs.lyt.db.business.service.impl.ItemServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ReportAnswerOptionServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ReportAnswerSatisfyServiceImpl;
import com.sunchs.lyt.framework.bean.IdTitleData;
import com.sunchs.lyt.framework.util.FormatUtil;
import com.sunchs.lyt.report.bean.*;
import com.sunchs.lyt.report.service.IReportCompareService;
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
    private ReportTargetService reportTargetService;

    @Autowired
    private ReportAnswerOptionServiceImpl reportAnswerOptionService;

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
    public ItemCompareData getItemCompareInfo(ItemCompareParam param) {
        ItemCompareData data = new ItemCompareData();
        if (Objects.isNull(param.getValueList()) || param.getValueList().size() == 0) {
            return data;
        }
        List<ItemCompareBean> valueList = param.getValueList();
        List<Integer> itemIds = valueList.stream().map(ItemCompareBean::getItemId).collect(Collectors.toList());
        Map<Integer, String> itemNameMap = getItemNameByIds(itemIds);

        List<ReportAnswerOption> tempQuestionOptionList = new ArrayList<>();
        List<SatisfyData> satisfyDataList = new ArrayList<>();
        List<IdTitleData> colList = new ArrayList<>();
        List<IdTitleData> rowList = new ArrayList<>();
        List<ItemCompareValue> vList = new ArrayList<>();
        // 设置列值
        valueList.forEach(item->{
            // 设置满意度列和值
            Double satisfyValue = reportTargetService.getItemAllSatisfy(item.getItemId(), item.getOfficeType());
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

            List<ReportAnswerOption> tempOptionList = getItemAnswerOption(item.getItemId(), item.getOfficeType(), item.getStartTime(), item.getEndTime());
            tempQuestionOptionList.addAll(tempOptionList);
        });
        data.setAllSatisfyList(satisfyDataList);
        data.setColList(colList);

        // 判断是否有题目
        if (tempQuestionOptionList.size() == 0) {
            return data;
        }

        Map<Integer, List<ReportAnswerOption>> questionMap = tempQuestionOptionList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getQuestionId));
        for (List<ReportAnswerOption> questionGroup : questionMap.values()) {
            Map<Integer, List<ReportAnswerOption>> itemQuestionList = questionGroup.stream().collect(Collectors.groupingBy(ReportAnswerOption::getItemId));
            // 添加行的基本信息
            if (questionGroup.size() > 0) {
                IdTitleData tData = new IdTitleData();
                tData.setId(questionGroup.get(0).getQuestionId());
                tData.setTitle(questionGroup.get(0).getQuestionName());
                rowList.add(tData);
            }
            // 最终值
            for (IdTitleData col : data.getColList()) {
                List<ReportAnswerOption> optionList = itemQuestionList.get(col.getId());
                if (Objects.nonNull(optionList)) {
                    // 计算满意度
                    double value = 0;
                    int number = 0;
                    Map<Integer, List<ReportAnswerOption>> tempOptionMap = optionList.stream().collect(Collectors.groupingBy(ReportAnswerOption::getOptionId));
                    for (Integer oid : tempOptionMap.keySet()) {
                        List<ReportAnswerOption> opList = tempOptionMap.get(oid);
                        if (Objects.nonNull(opList)) {
                            value += opList.get(0).getScore().doubleValue() * (double) opList.size();
                            number += opList.size();
                        }
                    }
                    if (number > 0) {
                        ItemCompareValue vObj = new ItemCompareValue();
                        vObj.setRowId(questionGroup.get(0).getQuestionId());
                        vObj.setColId(col.getId());
                        vObj.setValue(new BigDecimal(value / (double) number / 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        vList.add(vObj);
                    }
                }
            }
        }
        data.setValueList(vList);
        return data;
    }

    private List<ReportAnswerOption> getItemAnswerOption(Integer itemId, Integer optionType, String startTime, String endTime) {
        Date sTime = FormatUtil.dateTime(startTime);
        Date eTime = FormatUtil.dateTime(endTime);
        Wrapper<ReportAnswerOption> wrapper = new EntityWrapper<ReportAnswerOption>()
                .eq(ReportAnswerOption.ITEM_ID, itemId)
                .eq(ReportAnswerOption.OFFICE_TYPE_ID, optionType)
                .ge(ReportAnswerOption.ENDTIME, sTime)
                .le(ReportAnswerOption.ENDTIME, eTime)
                .in(ReportAnswerOption.OPTION_TYPE, Arrays.asList(1, 4));
        return reportAnswerOptionService.selectList(wrapper);
    }

    private List<ReportAnswerSatisfy> getItemAnswerInfo(Integer itemId, String startTime, String endTime) {
        Date sTime = FormatUtil.dateTime(startTime);
        Date eTime = FormatUtil.dateTime(endTime);
        Wrapper<ReportAnswerSatisfy> satisfyWrapper = new EntityWrapper<ReportAnswerSatisfy>()
                .setSqlSelect(
                        ReportAnswerSatisfy.QUESTION_ID + " as questionId",
                        ReportAnswerSatisfy.QUESTION_NAME + " as questionName",
                        ReportAnswerSatisfy.TARGET_THREE + " as targetThree",
                        "AVG(score as score)"
                )
                .eq(ReportAnswerSatisfy.ITEM_ID, itemId)
//                .lt(ReportAnswerSatisfy.)
                .groupBy(ReportAnswerSatisfy.QUESTION_ID);
        return reportAnswerSatisfyService.selectList(satisfyWrapper);
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
