package com.sunchs.lyt.report.service;

import com.sunchs.lyt.framework.bean.TitleValueData;
import com.sunchs.lyt.report.bean.ItemCrowdParam;
import com.sunchs.lyt.report.bean.SatisfyData;

import java.util.List;

public interface IReportOptionService {
//    /**
//     * 根据 选项ID 获取人群（题目满意度）
//     */
//    List<SatisfyData> getItemCrowdSatisfy(ItemCrowdParam param);

    /**
     * 根据 选项ID 获取人群（答卷满意度）
     */
    TitleValueData getItemCrowdAnswerSatisfy(ItemCrowdParam param);
}
