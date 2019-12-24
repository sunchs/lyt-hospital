package com.sunchs.lyt.report.service;

import com.sunchs.lyt.db.business.entity.Item;

import java.util.List;

public interface IReportCompareService {

    /**
     * 根据 科室类型ID 获取题目满意度表的项目列表
     */
    List<Item> getItemListByOfficeType(Integer officeType);
}
