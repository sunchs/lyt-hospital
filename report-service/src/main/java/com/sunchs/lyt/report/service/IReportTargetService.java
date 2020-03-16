package com.sunchs.lyt.report.service;

import com.sunchs.lyt.report.bean.SatisfyData;
import com.sunchs.lyt.report.bean.TotalParam;

import java.util.List;

public interface IReportTargetService {

    /**
     * 根据 指标ID 获取项目统计信息
     */
    List<SatisfyData> getItemSatisfyByTarget(int itemId, int targetId, int position);

    List<SatisfyData> getItemOfficeSatisfy(TotalParam param);

    List<SatisfyData> getItemOfficeTargetSatisfy(TotalParam param);

    /**
     * 获取总体满意度
     */
    Double getItemAllSatisfy(Integer itemId, Integer officeType);

    /**
     * 满意度和推荐度
     */
//    getItemSatisfyByOnly(TotalParam param);
}
