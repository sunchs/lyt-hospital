package com.sunchs.lyt.report.service;

import com.sunchs.lyt.report.bean.OutputParam;

public interface IReportOutputService {

    /**
     * 获取项目科室答卷
     */
    String getItemOfficeAnswer(OutputParam param);

    /**
     * 导出项目指标的对应标签数据
     */
    String getItemTargetTag(OutputParam param);
}
