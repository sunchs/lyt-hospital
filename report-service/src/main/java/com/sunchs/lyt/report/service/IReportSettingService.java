package com.sunchs.lyt.report.service;

import com.sunchs.lyt.framework.bean.SelectChildData;
import com.sunchs.lyt.framework.bean.TitleData;

import java.util.List;
import java.util.Map;

public interface IReportSettingService {

    /**
     * 获取项目已使用的问卷
     */
    List<TitleData> getItemUseQuestionnaireList(Integer itemId);

    /**
     *
     */
    List<Map<String, Object>> getItemUseAllList(Integer itemId);

    /**
     * 根据 问卷ID 获取答卷中的指标
     */
    List<Map<String, Object>> getItemQuestionnaireUseTarget(Integer itemId, Integer id);
}
