package com.sunchs.lyt.report.service;

import com.sunchs.lyt.report.bean.ItemRelatedData;

public interface IReportRelatedService {

    /**
     * 获取相关系数数据
     */
    ItemRelatedData getItemRelatedData(Integer itemId, Integer officeType);
}
