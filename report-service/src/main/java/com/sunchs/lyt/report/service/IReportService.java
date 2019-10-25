package com.sunchs.lyt.report.service;

import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.report.bean.ItemTotalData;
import com.sunchs.lyt.report.bean.ItemTotalParam;

public interface IReportService {

    /**
     * 问卷抽样量统计列表
     */
    PagingList<ItemTotalData> getItemTotalList(ItemTotalParam param);

}
