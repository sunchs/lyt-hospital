package com.sunchs.lyt.report.service;

import com.sunchs.lyt.report.bean.TotalSexData;

import java.util.List;

public interface IReportTagService {

    /**
     * 根据 标签ID 获取项目统计信息
     */
    List<TotalSexData> getItemQuantityByTag(int itemId, int tagId, int targetOne);

    /**
     * 根据 指标ID 获取项目统计信息
     */
    List<TotalSexData> getItemQuantityByTarget(int itemId, int targetId, int position);
}
