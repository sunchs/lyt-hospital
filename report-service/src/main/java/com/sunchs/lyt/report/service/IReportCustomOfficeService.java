package com.sunchs.lyt.report.service;

import com.sunchs.lyt.report.bean.CustomOfficeData;

import java.util.List;

public interface IReportCustomOfficeService {

    /**
     * 自定义科室满意度列表
     */
    List<CustomOfficeData> getCustomOfficeList(Integer itemId, Integer officeType);
}
