package com.sunchs.lyt.report.service;

import com.sunchs.lyt.report.bean.SatisfyData;

import java.util.List;

public interface IReportTargetService {

    /**
     * 根据 指标ID 获取项目统计信息
     */
    List<SatisfyData> getItemSatisfyByTarget(int itemId, int targetId, int position);
}
