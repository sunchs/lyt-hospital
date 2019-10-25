package com.sunchs.lyt.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.Answer;
import com.sunchs.lyt.db.business.entity.Item;
import com.sunchs.lyt.db.business.entity.ReportAnswer;
import com.sunchs.lyt.db.business.service.impl.AnswerServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ItemServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ReportAnswerServiceImpl;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.util.ObjectUtil;
import com.sunchs.lyt.framework.util.PagingUtil;
import com.sunchs.lyt.report.bean.ItemTotalData;
import com.sunchs.lyt.report.bean.ItemTotalParam;
import com.sunchs.lyt.report.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.dc.pr.PRError;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService implements IReportService {

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private AnswerServiceImpl answerService;

    @Autowired
    private ReportAnswerServiceImpl reportAnswerService;

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