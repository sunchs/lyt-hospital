package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.Item;
import com.sunchs.lyt.db.business.entity.ReportAnswerSatisfy;
import com.sunchs.lyt.db.business.service.impl.ItemServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ReportAnswerSatisfyServiceImpl;
import com.sunchs.lyt.report.service.IReportCompareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
