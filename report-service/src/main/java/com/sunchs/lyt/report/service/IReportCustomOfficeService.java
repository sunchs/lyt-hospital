package com.sunchs.lyt.report.service;

import com.sunchs.lyt.report.bean.CustomOfficeDataVO;

public interface IReportCustomOfficeService {

    /**
     * 自定义科室满意度列表
     */
    CustomOfficeDataVO getCustomOfficeList(Integer itemId, Integer officeType);
}
