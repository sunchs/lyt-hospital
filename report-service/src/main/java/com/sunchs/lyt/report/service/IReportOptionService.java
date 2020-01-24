package com.sunchs.lyt.report.service;

import com.sunchs.lyt.report.bean.ItemCrowdParam;
import com.sunchs.lyt.report.bean.SatisfyData;

import java.util.List;

public interface IReportOptionService {
    /**
     * 根据 选项ID 获取人群满意度
     */
    List<SatisfyData> getItemCrowdSatisfy(ItemCrowdParam param);
}
