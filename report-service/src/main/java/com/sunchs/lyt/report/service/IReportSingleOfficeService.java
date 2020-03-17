package com.sunchs.lyt.report.service;

import com.sunchs.lyt.report.bean.OutputParam;
import com.sunchs.lyt.report.bean.SingleOfficeSatisfyData;

public interface IReportSingleOfficeService {

    SingleOfficeSatisfyData getItemSingleOfficeSatisfy(Integer itemId, Integer officeType, Integer officeId);

    void setItemOfficeRanking(Integer itemId);

    /**
     * 导出xls
     */
    String outputSingleOfficeSatisfy(OutputParam param);
}
