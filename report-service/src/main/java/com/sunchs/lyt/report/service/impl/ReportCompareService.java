package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.Item;
import com.sunchs.lyt.db.business.entity.ReportAnswerSatisfy;
import com.sunchs.lyt.db.business.service.impl.ItemServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ReportAnswerSatisfyServiceImpl;
import com.sunchs.lyt.framework.util.FormatUtil;
import com.sunchs.lyt.report.bean.ItemCompareBean;
import com.sunchs.lyt.report.bean.ItemCompareData;
import com.sunchs.lyt.report.bean.ItemCompareParam;
import com.sunchs.lyt.report.service.IReportCompareService;
import freemarker.template.utility.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportCompareService implements IReportCompareService {

    @Autowired
    private ReportAnswerSatisfyServiceImpl reportAnswerSatisfyService;

    @Autowired
    private ItemServiceImpl itemService;

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
    public List<ItemCompareData> getItemCompareInfo(ItemCompareParam param) {
        List<ItemCompareData> result = new ArrayList<>();
        if (Objects.isNull(param.getValueList()) || param.getValueList().size() == 0) {
            return result;
        }



        List<ItemCompareBean> valueList = param.getValueList();
        List<Integer> itemIds = valueList.stream().map(ItemCompareBean::getItemId).collect(Collectors.toList());
        Map<Integer, String> itemNameMap = getItemNameByIds(itemIds);

        valueList.forEach(item->{
            ItemCompareData data = new ItemCompareData();
            data.setId(item.getItemId());
            data.setTitle(itemNameMap.get(item.getItemId()));
//            data.setValueList();

//            List<ReportAnswerSatisfy> itemAnswerInfo = getItemAnswerInfo(item.getItemId(), item.getStartTime(), item.getEndTime());
//            itemAnswerInfo
            result.add(data);
        });
        return result;
    }

    private List<ReportAnswerSatisfy> getItemAnswerInfo(Integer itemId, String startTime, String endTime) {
        Date sTime = FormatUtil.dateTime(startTime);
        Date eTime = FormatUtil.dateTime(endTime);
        Wrapper<ReportAnswerSatisfy> satisfyWrapper = new EntityWrapper<ReportAnswerSatisfy>()
                .setSqlSelect(
                        ReportAnswerSatisfy.QUESTION_ID + " as questionId",
                        ReportAnswerSatisfy.QUESTION_NAME + " as questionName",
                        ReportAnswerSatisfy.TARGET_THREE + " as targetThree",
                        "AVG(score as score"
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
